@Transactional
@Service
public class SearchService implements ApplicationListener<ApplicationReadyEvent> {

    public static final String FACET_CATEGORY = "category";
    public static final String FACET_CREATED_YEAR = "createdYear";
    public static final String FIELD_TEXT = "text";

    private final EntityManager entityManager;


    @Autowired
    public SearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public SearchResponse search(SearchRequest request) {


        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(AdData.class).get();

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
            Query dateQuery = queryBuilder.keyword().onField(FIELD_TEXT).matching(request.keyword).createQuery();
            bool = bool.must(dateQuery);
        }

        FacetingRequest categoryFacetRequest = queryBuilder.facet().name(FACET_CATEGORY).onField(FACET_CATEGORY)
                .discrete().createFacetingRequest();

        FacetingRequest dateYearFacetRequest = queryBuilder.facet().name(FACET_CREATED_YEAR).onField(FACET_CREATED_YEAR)
                .discrete().createFacetingRequest();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(bool.createQuery(), AdData.class);
        fullTextQuery.getFacetManager().enableFaceting(categoryFacetRequest);
        fullTextQuery.getFacetManager().enableFaceting(dateYearFacetRequest);

        List<SearchFacet> categoryFacet = fullTextQuery.getFacetManager().getFacets(FACET_CATEGORY)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());
        List<SearchFacet> createdFacet = fullTextQuery.getFacetManager().getFacets(FACET_CREATED_YEAR)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());

        return new SearchResponse(createdFacet, categoryFacet, fullTextQuery.getResultList());

    }

}
