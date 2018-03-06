package at.willhaben.test.search.dto;


public class SearchRequest {

    public final String createdYear;

    public final String category;

    public final String keyword;

    public SearchRequest(String createdYear, String category, String keyword) {
        this.createdYear = createdYear;
        this.category = category;
        this.keyword = keyword;
    }

    public static SearchRequest all() {
        return new SearchRequest(null, null, null);
    }

    public SearchRequest searchCategory(String category) {
        return new SearchRequest(this.createdYear, category, this.keyword);
    }

    public SearchRequest searchCreatedYear(String createdYear) {
        return new SearchRequest(createdYear, this.category, this.keyword);
    }

    public SearchRequest searchKeyword(String keyword) {
        return new SearchRequest(this.createdYear, this.category, keyword);
    }
}
