package io.gitlab.druzyna_a.knowledgebase.db;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 *
 * @author Damian Terlecki
 */
public interface ArticleRepository extends Repository<ArticlesRequest, String> {

    void delete(ArticlesRequest deleted);
    
    List<ArticlesRequest> findAll();
    
    List<ArticlesRequest> findByScraped(boolean scraped);
    List<ArticlesRequest> findByScrapedOrderByTimeAsc(boolean scraped);
    List<ArticlesRequest> findByScrapedOrderByTimeDesc(boolean scraped);

    Optional<ArticlesRequest> findOne(String id);

    ArticlesRequest save(ArticlesRequest saved);
    
    long countByScraped(boolean scraped);
}
