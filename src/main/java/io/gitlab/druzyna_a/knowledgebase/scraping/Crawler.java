package io.gitlab.druzyna_a.knowledgebase.scraping;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Damian Terlecki
 */
public class Crawler implements Runnable {

    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final int PAGES_DEPTH = 3;
    private static final int THREAD_POOL = 3;
    private final String baseUrl;
    private final ScrapeCommand scrapeCommand;
    private final int pagesDepth;
    private boolean main;
    private List<String> visitedPages;
    private ExecutorService executorService;
    private Semaphore sem;

    /**
     * Run only one at a time
     *
     * @param baseUrl
     * @param scrapeCommand
     */
    public Crawler(String baseUrl, ScrapeCommand scrapeCommand) {
        this(baseUrl, scrapeCommand, PAGES_DEPTH);
        main = true;
        executorService = Executors.newFixedThreadPool(THREAD_POOL);
        sem = new Semaphore(THREAD_POOL + 1);
        visitedPages = Collections.synchronizedList(new LinkedList<>());
    }

    private Crawler(String baseUrl, ScrapeCommand scrapeCommand, int pagesDepth) {
        this.baseUrl = baseUrl;
        this.scrapeCommand = scrapeCommand;
        this.pagesDepth = pagesDepth;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ((!main && scrapeCommand.ignore(baseUrl))
                || pagesDepth == 0 || visitedPages.contains(baseUrl.replace("/", ""))) {
            sem.release();
            return;
        }
        try {
            System.out.println("CONNECTING TO: " +baseUrl);
            final Connection connection = Jsoup.connect(baseUrl).timeout(10 * 1000).userAgent(USER_AGENT);
            final Document doc = connection.get();
            final String baseUri = doc.baseUri().replace("/", "");
            if (visitedPages.contains(baseUri)) {
                sem.release();
                return;
            } else {
                visitedPages.add(baseUri);
                visitedPages.add(baseUrl.replace("/", ""));
            }

            System.out.println("Im at " + pagesDepth + " depth on: " + doc.baseUri());
            scrapeCommand.scrape(doc);
            doc.select("a[href]").stream().filter(l -> !l.attr("href").equals("#") && !l.absUrl("href").isEmpty() && !visitedPages.contains(l.absUrl("href").replace("/", ""))).forEach(l -> {
                final String url = l.absUrl("href");
                final Crawler crawler = new Crawler(url, scrapeCommand, pagesDepth - 1);
                crawler.setExecutorService(executorService);
                crawler.setVisitedPages(visitedPages);
                crawler.setSemaphore(sem);
                executorService.execute(crawler);
            });
            if (main) {
                try {
                    while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        if (sem.tryAcquire()) {
                            executorService.shutdown();
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        sem.release();
    }

    private void setVisitedPages(List<String> visitedPages) {
        this.visitedPages = visitedPages;
    }

    private void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setSemaphore(Semaphore sem) {
        this.sem = sem;
    }

}
