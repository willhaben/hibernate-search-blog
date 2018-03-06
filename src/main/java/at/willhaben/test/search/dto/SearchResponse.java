package at.willhaben.test.search.dto;


import at.willhaben.test.search.models.AdData;
import org.hibernate.search.query.facet.Facet;

import java.util.List;

public class SearchResponse {

    public final List<SearchFacet> createdYearFacet;

    public final List<SearchFacet> categoryFacet;

    public final List<AdData> result;


    public SearchResponse(List<SearchFacet> createdYearFacet, List<SearchFacet> categoryFacet, List<AdData> result) {
        this.createdYearFacet = createdYearFacet;
        this.categoryFacet = categoryFacet;
        this.result = result;
    }
}
