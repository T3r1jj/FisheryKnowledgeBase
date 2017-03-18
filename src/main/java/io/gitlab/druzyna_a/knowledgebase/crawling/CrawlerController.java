package io.gitlab.druzyna_a.knowledgebase.crawling;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest;
import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest.Article;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import io.gitlab.druzyna_a.knowledgebase.db.ArticlesRepository;

/**
 *
 * @author Damian Terlecki
 */
@Component
@Scope("singleton")
public class CrawlerController {

    @Autowired
    private ArticlesRepository articlesRepository;
    private ArticlesRequest articlesRequest;
    private final Crawler[] crawlers = new Crawler[2];
    private final Semaphore runPermission = new Semaphore(1);

    public CrawlerController() {
    }

    /**
     * Will run crawlers to scrape articles from the web. If crawlers are
     * already running the method will return.
     */
    @Async
    public void run() {
        if (!runPermission.tryAcquire()) {
            return;
        }
        Optional<ArticlesRequest> possibleRequest = articlesRepository.findFirstByScrapedOrderByTimeAsc(false);
        try {
            while (possibleRequest.isPresent()) {
                articlesRequest = possibleRequest.get();
                initCrawlers();
                startCrawlers();
                articlesRequest.setScraped(true);
                articlesRepository.save(articlesRequest);
                possibleRequest = articlesRepository.findFirstByScrapedOrderByTimeAsc(false);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(CrawlerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        runPermission.release();
    }

    private void startCrawlers() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(crawlers.length);
        for (Crawler crawler : crawlers) {
            if (crawler != null) {
                executorService.execute(crawler);
            }
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(5, TimeUnit.SECONDS));
    }

    private void initCrawlers() {
        ScrapeCommand scrapeCommand = initScrapingCommand();
        if (articlesRequest.isQuick()) {
            crawlers[0] = new Crawler("https://www.google.com/search?num=90&q=" + getEncodedQueryParams(), scrapeCommand, 2, 5);
        } else {
            crawlers[0] = new Crawler("https://www.google.com/search?q=" + getEncodedQueryParams(), scrapeCommand, 3, 2);
            crawlers[1] = new Crawler("https://en.wikipedia.org/wiki/Index_of_fishing_articles", scrapeCommand, 2, 1);
        }
    }

    private ScrapeCommand initScrapingCommand() {
        List<Article> articles = Collections.synchronizedList(new LinkedList<>());
        articlesRequest.setArticles(articles);
        ScrapeCommand scrapeCommand = new SmartScrapeCommand() {
            @Override
            public void scrape(Document document) {
                Elements divs = document.select("div");
                Elements innerMostDivs = new Elements();
                divs.stream().filter(div -> div.select(">div").isEmpty()).forEach(div -> innerMostDivs.add(div));
                final List<Element> resultDivs = innerMostDivs.stream().filter(div -> {
                    return articlesRequest.getTags().stream().filter(tag -> div.text().toLowerCase().contains(tag.toLowerCase())).count() >= articlesRequest.getRequiredTagsCount()
                            && isPotentiallyValuable(div);

                }).collect(Collectors.toList());
                Article article = new Article();
                article.setTitle(document.title());
                article.setAuthor(document.baseUri());
                article.setImage(scrapeImage(document));
                resultDivs.forEach(div -> {
                    article.appendDescription(scrapeDescription(div));
                });
                if (!article.isEmpty()) {
                    articles.add(article);
                }
            }
        };
        return scrapeCommand;
    }

    private boolean isPotentiallyValuable(Element div) {
        int wordsCount = div.text().split(" ").length;
        int linksCount = div.getElementsByTag("a").size();
        int commasCount = div.text().split(",").length;
        return wordsCount > 3 * linksCount && wordsCount > 5 * commasCount;
    }

    private String scrapeDescription(Element div) {
        div.getElementsByTag("a").stream().filter(a -> a.attr("href").contains("edit")).forEach(a -> a.remove());
        return div.toString();
    }

    private String scrapeImage(Document document) {
        Elements metaOgImage = document.select("meta[property=og:image]");
        return (metaOgImage != null) ? metaOgImage.attr("content") : null;
    }

    private String getEncodedQueryParams() {
        try {
            return URLEncoder.encode(articlesRequest.getTags().stream().collect(Collectors.joining(" ")), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CrawlerController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

}
