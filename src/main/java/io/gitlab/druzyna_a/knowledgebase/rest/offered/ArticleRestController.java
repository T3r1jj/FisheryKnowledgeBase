package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.crawling.CrawlerController;
import io.gitlab.druzyna_a.knowledgebase.db.ArticlesRepositoryCleaner;
import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest;
import io.gitlab.druzyna_a.knowledgebase.scraping.TagsScraper;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest.Article;
import io.gitlab.druzyna_a.totp4j.TOTP;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import io.gitlab.druzyna_a.knowledgebase.db.ArticlesRepository;
import java.time.Instant;
import java.util.ArrayList;
import static io.gitlab.druzyna_a.knowledgebase.db.ArticlesRepository.EPOCH_SEC_QUICK_SEARCH;
import static io.gitlab.druzyna_a.knowledgebase.db.ArticlesRepository.EPOCH_SEC_LONG_SEARCH;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class ArticleRestController implements ArticleApi {

    @Autowired
    private TagsScraper tagsScraper;
    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private CrawlerController crawlerController;
    @Autowired
    private ArticlesRepositoryCleaner articlesCleaner;

    @Value("${io.gitlab.druzyna_a.knowledgebase.totp_interval}")
    private int totpInterval;
    @Value("${io.gitlab.druzyna_a.knowledgebase.totp_key}")
    private String totpKey;
    @Value("${io.gitlab.druzyna_a.knowledgebase.totp_token_length}")
    private int totpTokenLength;
    @Value("${io.gitlab.druzyna_a.knowledgebase.totp_hmac_algorithm}")
    private String totpHmacAlgorithm;

    @Override
    public ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Id of articles request", required = true) @PathVariable String id,
            @ApiParam(value = "API token", required = true) @RequestParam int token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(403).build();
        }
        Optional<ArticlesRequest> possibleRequest = articlesRepository.findOne(id);
        if (possibleRequest.isPresent()) {
            ArticlesRequest articlesRequest = possibleRequest.get();
            if (articlesRequest.isScraped()) {
                List<Article> articles = articlesRequest.getArticles();
                return ResponseEntity.ok(articles);
            } else {
                return ResponseEntity.status(202).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<List<ArticlesRequest>> fetchArticlesRequests(@ApiParam(value = "API token", required = true) @RequestParam int token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(403).build();
        }
        List<ArticlesRequest> articlesRequests = articlesRepository.findAll();
        articlesRequests.stream().forEach(ar -> ar.setArticles(new ArrayList<>()));
        return ResponseEntity.ok(articlesRequests);
    }

    @Override
    public ResponseEntity<String> requestArticles(@ApiParam(value = "Tags", required = true) @RequestParam List<String> tags,
            @ApiParam(value = "API token", required = true) @RequestParam int token,
            @ApiParam(value = "Is it a quick search? Quick search takes less than 15 minutes while slow lasts from 15 to 150 minutes depending on the connection speed.",
                    defaultValue = "true", allowableValues = "true, false", required = true) @RequestParam() boolean quick,
            @ApiParam(value = "Minimal number of tags required to be found in article", defaultValue = "1") @RequestParam(required = false) int requiredTagsCount) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(403).build();
        }
        if (articlesRepository.countByScraped(false) > UNSCRAPED_ARTICLES_QUEUE_LIMIT) {
            return ResponseEntity.status(429).build();
        }
        ArticlesRequest articleRequest = new ArticlesRequest(tags, Math.min(tags.size(), Math.max(requiredTagsCount, 1)));
        articleRequest.setQuick(quick);
        articleRequest.setScraped(false);
        long estimatedSec = EPOCH_SEC_QUICK_SEARCH * articlesRepository.countByScrapedAndQuick(false, true) + EPOCH_SEC_LONG_SEARCH * articlesRepository.countByScrapedAndQuick(false, false)
                + (quick ? EPOCH_SEC_QUICK_SEARCH : EPOCH_SEC_LONG_SEARCH);
        articleRequest.setEstimatedTime(Instant.now().getEpochSecond() + estimatedSec);
        String id = articlesRepository.save(articleRequest).getId();
        crawlerController.run();
        return ResponseEntity.ok(id);
    }

    @Override
    public ResponseEntity<List<String>> fetchTags(@ApiParam(value = "Tags type", allowableValues = "Rods, Reels, Lures, Accessories, Tackles, Fishing, All", defaultValue = "All", required = true)
            @PathVariable(value = "type") String tagsType) {
        return ResponseEntity.ok(tagsScraper.scrapeTags(TagsScraper.Tags.valueOf(tagsType.toUpperCase())));
    }

    private boolean isTokenValid(int token) {
        articlesCleaner.run();
        crawlerController.run();
        try {
            return new TOTP.Builder()
                    .setInterval(totpInterval)
                    .setKey(totpKey)
                    .setT0(System.currentTimeMillis() / 1000)
                    .setTokenLength(totpTokenLength)
                    .setAlgorithm(totpHmacAlgorithm)
                    .createTOTP().isTokenValid(token);
        } catch (InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(ArticleRestController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
