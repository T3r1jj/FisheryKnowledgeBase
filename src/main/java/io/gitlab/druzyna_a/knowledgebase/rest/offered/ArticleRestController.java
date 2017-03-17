package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.Article;
import io.gitlab.druzyna_a.knowledgebase.scraping.TagsScraper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TagsScraper tagsScraper;

    @Override
    public ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request") @RequestParam Long id,
            @ApiParam(value = "API token", required = true) @RequestParam String token) {
        return ResponseEntity.ok().body(Arrays.asList(new Article[]{new Article(), new Article()}));
    }

    @Override
    public ResponseEntity<Long> requestArticles(@ApiParam(value = "Tags", required = true) @RequestParam List<String> tags,
            @ApiParam(value = "API token", required = true) @RequestParam String token,
            @ApiParam(value = "Minimal number of tags required to be found in article", defaultValue = "1") @RequestParam(required = false) int minTagsCount) {
        return ResponseEntity.ok().body(new Long(0));
    }

    @Override
    public ResponseEntity<List<String>> fetchRodTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.RODS));
    }

    @Override
    public ResponseEntity<List<String>> fetchReelTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.REELS));
    }

    @Override
    public ResponseEntity<List<String>> fetchLureTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.LURES));
    }

    @Override
    public ResponseEntity<List<String>> fetchAccesssoryTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.ACCESSORIES));
    }

    @Override
    public ResponseEntity<List<String>> fetchTackleTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.TACKLE));
    }
    
    @Override
    public ResponseEntity<List<String>> fetchFishingTags() {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.FISHING));
    }

}
