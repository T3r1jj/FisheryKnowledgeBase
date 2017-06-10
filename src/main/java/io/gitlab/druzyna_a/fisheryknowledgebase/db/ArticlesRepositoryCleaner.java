package io.gitlab.druzyna_a.fisheryknowledgebase.db;

import static io.gitlab.druzyna_a.fisheryknowledgebase.db.ArticlesRepository.EPOCH_SEC_CACHE;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author Damian Terlecki
 */
@Component
@Scope("singleton")
public class ArticlesRepositoryCleaner {

    @Autowired
    private ArticlesRepository articlesRepository;

    @Async
    public void run() {
        articlesRepository.findByScrapedOrderByTimeAsc(true).stream().filter(a -> Instant.now().getEpochSecond() > a.getEstimatedTime()).forEach(a -> articlesRepository.delete(a));
    }

}
