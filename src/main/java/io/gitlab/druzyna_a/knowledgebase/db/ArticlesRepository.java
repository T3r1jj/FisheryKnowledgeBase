package io.gitlab.druzyna_a.knowledgebase.db;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 *
 * @author Damian Terlecki
 */
public interface ArticlesRepository extends Repository<ArticlesRequest, String> {

    static final long EPOCH_SEC_CACHE = 60 * 60 * 6;
    static final long EPOCH_SEC_QUICK_SEARCH = 60 * 15;
    static final long EPOCH_SEC_LONG_SEARCH = 60 * 150;

    void delete(ArticlesRequest deleted);

    List<ArticlesRequest> findAll();

    List<ArticlesRequest> findByScraped(boolean scraped);

    List<ArticlesRequest> findByScrapedOrderByTimeAsc(boolean scraped);

    List<ArticlesRequest> findByScrapedOrderByTimeDesc(boolean scraped);

    Optional<ArticlesRequest> findFirstByScrapedOrderByTimeAsc(boolean scraped);

    Optional<ArticlesRequest> findOne(String id);

    ArticlesRequest save(ArticlesRequest saved);

    long countByScraped(boolean scraped);

    long countByScrapedAndQuick(boolean scraped, boolean isQuick);
}
