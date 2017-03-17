package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.Article;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(httpMethod = "GET", value = "Fetch articles by assigned request id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of found articles", response = Article.class, responseContainer = "List")
        , @ApiResponse(code = 202, message = "Searching for articles...", response = Void.class)
        , @ApiResponse(code = 403, message = "Invalid token", response = Void.class)
        , @ApiResponse(code = 404, message = "Articles not found", response = Void.class)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request", required = true) @RequestParam Long id,
            @ApiParam(value = "API token", required = true) @RequestParam String token);

    @ApiOperation(httpMethod = "POST", value = "Request articles tags. The request will queued and associated id will be returned. Refer to other resoruce for articles fetching.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Request id", response = Long.class)
        ,
        @ApiResponse(code = 403, message = "Invalid token", response = Void.class)
    })
    @RequestMapping(path = "queue", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<Long> requestArticles(@ApiParam(value = "Tags", required = true) @RequestParam List<String> tags,
            @ApiParam(value = "API token", required = true) @RequestParam String token,
            @ApiParam(value = "Minimal number of tags required to be found in article", defaultValue = "1") @RequestParam(required = false) int minTagsCount);

    @ApiOperation(httpMethod = "GET", value = "Fetch rod tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/rod", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchRodTags();

    @ApiOperation(httpMethod = "GET", value = "Fetch reel tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/reel", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchReelTags();

    @ApiOperation(httpMethod = "GET", value = "Fetch lure tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/lure", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchLureTags();

    @ApiOperation(httpMethod = "GET", value = "Fetch accesory tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/accessory", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchAccesssoryTags();

    @ApiOperation(httpMethod = "GET", value = "Fetch tackle tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/tackle", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchTackleTags();

    @ApiOperation(httpMethod = "GET", value = "Fetch tackle tags that could be used when requesting articles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of tags", response = String.class, responseContainer = "List")})
    @RequestMapping(path = "tags/fishing", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchFishingTags();

}
