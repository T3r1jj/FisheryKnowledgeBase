package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.Article;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class ArticleRestController implements ArticleApi {

    @ApiOperation(httpMethod = "GET", value = "Fetch articles by assigned request id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of found articles", response = Article.class, responseContainer = "List")
        ,
        @ApiResponse(code = 202, message = "Searching for articles...", response = Void.class)
        ,
        @ApiResponse(code = 404, message = "Articles not found", response = Void.class)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @Override
    public ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request") @RequestParam Long id) {
        return ResponseEntity.ok().body(Arrays.asList(new Article[]{new Article(), new Article()}));
    }

    @ApiOperation(httpMethod = "GET", value = "Request articles by title, description and/or author. The request will queued and associated id will be returned. Refer to other resoruce for articles fetching.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Request id", response = Long.class)})
    @RequestMapping(path = "queue", method = RequestMethod.POST, produces = "application/json")
    @Override
    public ResponseEntity<Long> requestArticles(@ApiParam(value = "Regex for searching by title") @RequestParam(required = false) String titleRegex,
            @ApiParam(value = "Regex for searching by description") @RequestParam(required = false) String descriptionRegex,
            @ApiParam(value = "Regex for searching by author") @RequestParam(required = false) String authorRegex) {
        return ResponseEntity.ok().body(new Long(0));
    }

}
