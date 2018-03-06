package at.willhaben.test.search.service;

import at.willhaben.test.search.dto.SearchRequest;
import at.willhaben.test.search.dto.SearchResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApplicationTests {

	@Autowired
	private SearchService searchService;

	@Test
	public void emptySearch() {
		SearchResponse search = searchService.search(SearchRequest.all());
		assertEquals(2,search.categoryFacet.size());
		assertEquals(4,search.createdYearFacet.size());
		assertEquals(6,search.result.size());
	}

    @Test
    public void facetSearch() {
        SearchResponse search = searchService.search(SearchRequest.all().searchCategory("dogs"));
        assertEquals(1,search.categoryFacet.size());
        assertEquals(3,search.createdYearFacet.size());
        assertEquals(3,search.result.size());

        search = searchService.search(SearchRequest.all().searchCreatedYear("1999"));
        assertEquals(2,search.categoryFacet.size());
        assertEquals(1,search.createdYearFacet.size());
        assertEquals(2,search.result.size());

        search = searchService.search(SearchRequest.all().searchKeyword("At vero eos"));
        assertEquals(1,search.categoryFacet.size());
        assertEquals(2,search.createdYearFacet.size());
        assertEquals(2,search.result.size());
    }

    @Test
    public void drillDownSearch() {
        SearchRequest request = SearchRequest.all();
        SearchResponse search = searchService.search(request);
        assertTrue(search.result.size() > 0);

        request = request.searchCreatedYear(search.createdYearFacet.get(0).value);
        search = searchService.search(request);
        assertTrue(search.result.size() > 0);

        request = request.searchCategory(search.categoryFacet.get(0).value);
        search = searchService.search(request);
        assertTrue(search.result.size() > 0);

    }

}
