package io.gitlab.druzyna_a.knowledgebase.db;

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

    static final long EPOCH_SEC_CACHE = 60 * 60 * 6;

    @Autowired
    private ArticlesRepository articlesRepository;

    @Async
    public void run() {
        articlesRepository.findByScrapedOrderByTimeAsc(true).stream().filter(a -> Instant.now().getEpochSecond() - a.getTime() > EPOCH_SEC_CACHE).forEach(a -> articlesRepository.delete(a));
    }

}
