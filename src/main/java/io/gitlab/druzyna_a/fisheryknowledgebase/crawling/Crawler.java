package io.gitlab.druzyna_a.fisheryknowledgebase.crawling;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Crawls web from baseUrl through links on given page to pagesDepth. Each
 * connection is run in thread from fixed pool. Before connecting to page the
 * crawler checks if page should be visited (ScrapeCommand), if true it invokes
 * scrape and for every valid link a new Scraper is instantiated to be run in
 * fixed thread pool.
 *
 * Use {@link ScrapeCommand} to implement scraping. Think about: 1. Black list
 * of webpages 2. Skipping already visited pages 3. Delay when accessing same
 * host to not put too much load on the web
 *
 * @author Damian Terlecki
 */
public class Crawler implements Runnable {

    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final int TIMEOUT_SEC = 10;
    private final String baseUrl;
    private final ScrapeCommand scrapeCommand;
    private final int pagesDepth;
    private int threadPool;
    private boolean main;
    private ExecutorService executorService;
    private Semaphore sem;

    public Crawler(String baseUrl, ScrapeCommand scrapeCommand, int pagesDepth, int threadPool) {
        this(baseUrl, scrapeCommand, pagesDepth);
        this.threadPool = threadPool;
        executorService = Executors.newFixedThreadPool(threadPool);
        sem = new Semaphore(threadPool + 1);
        main = true;
    }

    private Crawler(String baseUrl, ScrapeCommand scrapeCommand, int pagesDepth) {
        this.baseUrl = baseUrl;
        this.scrapeCommand = scrapeCommand;
        this.pagesDepth = pagesDepth;
    }

    @Override
    public void run() {
        try {
            sem.acquire();
            if (pagesDepth > 0 && (scrapeCommand.shouldVisit(baseUrl) || main)) {
                Connection connection = Jsoup.connect(baseUrl).timeout(TIMEOUT_SEC * 1000).userAgent(USER_AGENT);
                Document doc = connection.get();
                doc.select("a[href]").stream().filter(l -> !l.attr("href").equals("#") && !l.absUrl("href").isEmpty()).forEach(l -> {
                    String url = l.absUrl("href");
                    Crawler crawler = new Crawler(url, scrapeCommand, pagesDepth - 1);
                    crawler.setExecutorService(executorService);
                    crawler.setSemaphore(sem);
                    executorService.execute(crawler);
                });
                scrapeCommand.scrape(doc);
                if (main) {
                    while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        if (sem.tryAcquire(threadPool)) {
                            executorService.shutdown();
                        }
                    }
                }
            }
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        sem.release();
    }

    private void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private void setSemaphore(Semaphore sem) {
        this.sem = sem;
    }

    public static class Builder {

        private String baseUrl;
        private ScrapeCommand scrapeCommand;
        private int pagesDepth = 3;
        private int threadPool = 3;

        public Builder() {
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setScrapeCommand(ScrapeCommand scrapeCommand) {
            this.scrapeCommand = scrapeCommand;
            return this;
        }

        public Builder setPagesDepth(int pagesDepth) {
            this.pagesDepth = pagesDepth;
            return this;
        }

        public Builder setThreadPool(int threadPool) {
            this.threadPool = threadPool;
            return this;
        }

        public Crawler createCrawler() {
            return new Crawler(baseUrl, scrapeCommand, pagesDepth, threadPool);
        }

    }
}
