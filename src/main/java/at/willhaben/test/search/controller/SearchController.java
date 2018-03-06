package at.willhaben.test.search.controller;

import at.willhaben.test.search.dto.SearchRequest;
import at.willhaben.test.search.dto.SearchResponse;
import at.willhaben.test.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SearchResponse search(@RequestParam(value = "category", required = false) String cat,
                                 @RequestParam(value = "createdYear", required = false) String yearCreated,
                                 @RequestParam(value = "keyword", required = false) String keyword){
        return searchService.search(new SearchRequest(yearCreated, cat, keyword));
    }

}
