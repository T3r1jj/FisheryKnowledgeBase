package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest;
import io.gitlab.druzyna_a.knowledgebase.scraping.TagsScraper;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.gitlab.druzyna_a.knowledgebase.db.ArticleRepository;
import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest.Article;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class ArticleRestController implements ArticleApi {

    @Autowired
    private TagsScraper tagsScraper;
    @Autowired
    private ArticleRepository articlesRepository;

    @Override
    public ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request", required = true) @RequestParam String id,
            @ApiParam(value = "API token", required = true) @RequestParam String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(403).build();
        }
        Optional<ArticlesRequest> articlesRequest = articlesRepository.findOne(id);
        if (articlesRequest.isPresent()) {
            ArticlesRequest articles = articlesRequest.get();
            if (articles.isScraped()) {
                return ResponseEntity.ok(articles.getArticles());
            } else {
                return ResponseEntity.status(202).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<String> requestArticles(@ApiParam(value = "Tags", required = true) @RequestParam List<String> tags,
            @ApiParam(value = "API token", required = true) @RequestParam String token,
            @ApiParam(value = "Minimal number of tags required to be found in article", defaultValue = "1") @RequestParam(required = false) int requiredTagsCount) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(403).build();
        }
        if (articlesRepository.countByScraped(false) > UNSCRAPED_ARTICLES_QUEUE_LIMIT) {
            return ResponseEntity.status(429).build();
        }
        ArticlesRequest article = new ArticlesRequest(tags, requiredTagsCount);
        String id = articlesRepository.save(article).getId();
        return ResponseEntity.ok(id);
    }

    @Override
    public ResponseEntity<List<String>> fetchTags(@ApiParam(value = "Tags type", allowableValues = "Rods, Reels, Lures, Accessories, Tackles, Fishing, All", defaultValue = "All", required = true)
            @PathVariable(value = "type") String tagsType) {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.valueOf(tagsType.toUpperCase())));
    }

    private boolean isTokenValid(String token) {
        return true;
    }

}
