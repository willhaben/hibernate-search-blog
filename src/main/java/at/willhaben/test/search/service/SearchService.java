package at.willhaben.test.search.service;


import at.willhaben.test.search.dto.SearchFacet;
import at.willhaben.test.search.dto.SearchRequest;
import at.willhaben.test.search.dto.SearchResponse;
import at.willhaben.test.search.models.AdData;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class SearchService implements ApplicationListener<ApplicationReadyEvent> {

    public static final String FACET_CATEGORY = "category";
    public static final String FACET_CREATED_YEAR = "createdYear";

    private final EntityManager entityManager;


    @Autowired
    public SearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private void initData() throws InterruptedException {
        Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
    }


    public SearchResponse search(SearchRequest request) {


        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(AdData.class).get();


        FacetingRequest categoryFacetRequest = queryBuilder.facet().name(FACET_CATEGORY).onField(FACET_CATEGORY)
                .discrete().createFacetingRequest();


        FacetingRequest dateYearFacetRequest = queryBuilder.facet().name(FACET_CREATED_YEAR).onField(FACET_CREATED_YEAR)
                .discrete().createFacetingRequest();

        BooleanJunction bool = queryBuilder.bool().must(queryBuilder.all().createQuery());

        if (request.category != null) {
            Query categoryQuery = queryBuilder.keyword().onField(FACET_CATEGORY).matching(request.category).createQuery();
            bool = bool.must(categoryQuery);
        }
        if (request.createdYear != null) {
            Query dateQuery = queryBuilder.keyword().onField(FACET_CREATED_YEAR).matching(request.createdYear).createQuery();
            bool = bool.must(dateQuery);
        }
        if (request.keyword != null) {
            Query dateQuery = queryBuilder.keyword().onField("text").matching(request.keyword).createQuery();
            bool = bool.must(dateQuery);
        }


        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(bool.createQuery(), AdData.class);
        fullTextQuery.getFacetManager().enableFaceting(categoryFacetRequest);
        fullTextQuery.getFacetManager().enableFaceting(dateYearFacetRequest);

        List<SearchFacet> categoryFacet = fullTextQuery.getFacetManager().getFacets(FACET_CATEGORY)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());
        List<SearchFacet> createdFacet = fullTextQuery.getFacetManager().getFacets(FACET_CREATED_YEAR)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());
        ;

        return new SearchResponse(createdFacet, categoryFacet, fullTextQuery.getResultList());

    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
