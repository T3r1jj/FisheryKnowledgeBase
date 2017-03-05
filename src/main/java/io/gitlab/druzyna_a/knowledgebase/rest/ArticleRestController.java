package io.gitlab.druzyna_a.knowledgebase.rest;

import io.gitlab.druzyna_a.knowledgebase.model.Article;
import io.swagger.annotations.ApiParam;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class ArticleRestController implements ArticleApi {

    @Override
    public ResponseEntity<List<Article>> fetchArticles(@ApiParam(value = "Regex for searching by title") @RequestParam(required = false) String titleRegex,
            @ApiParam(value = "Regex for searching by description") @RequestParam(required = false) String descriptionRegex,
            @ApiParam(value = "Regex for searching by author") @RequestParam(required = false) String authorRegex) {
        return ResponseEntity.ok().body(Arrays.asList(new Article[]{new Article(), new Article()}));
    }
    
}
