package at.willhaben.test.search.dto;


public class SearchFacet {

    public final String value;
    public final Integer count;


    public SearchFacet(String value, Integer count) {
        this.value = value;
        this.count = count;
    }
}
