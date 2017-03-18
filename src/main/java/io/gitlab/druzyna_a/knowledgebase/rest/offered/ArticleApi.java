package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest.Article;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Damian Terlecki
 */
@Api(value = "/article", tags = {"article"}, description = "Articles about fishing from external resources")
@RequestMapping("/article")
public interface ArticleApi {

    static final int UNSCRAPED_ARTICLES_QUEUE_LIMIT = 100;

    @ApiOperation(httpMethod = "GET", value = "Fetch articles by assigned request id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of found articles", response = Article.class, responseContainer = "List")
        , @ApiResponse(code = 202, message = "Pending search...", response = Void.class)
        , @ApiResponse(code = 403, message = "Invalid token", response = Void.class)
        , @ApiResponse(code = 404, message = "Request not found", response = Void.class)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request", required = true) @RequestParam String id,
            @ApiParam(value = "API token", required = true) @RequestParam int token);

    @ApiOperation(httpMethod = "POST", value = "Request articles scrap by tags. The request will queued and associated id will be returned. Refer to other resoruce for articles fetching.", consumes = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Request id", response = String.class)
        , @ApiResponse(code = 403, message = "Invalid token", response = Void.class)
        , @ApiResponse(code = 429, message = "Too many requests pending", response = Void.class)
    })
    @RequestMapping(path = "request", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> requestArticles(@ApiParam(value = "Tags", required = true) @RequestParam List<String> tags,
            @ApiParam(value = "API token", required = true) @RequestParam int token,
            @ApiParam(value = "Minimal number of tags required to be found in article", defaultValue = "1") @RequestParam(required = false) int requiredTagsCount);

    @ApiOperation(httpMethod = "GET", value = "Fetch rod tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/{type}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchTags(@ApiParam(value = "Tags type", allowableValues = "Rods, Reels, Lures, Accessories, Tackles, Fishing, All", defaultValue = "All")
            @PathVariable(required = false, value = "type") String tagsType);

}
