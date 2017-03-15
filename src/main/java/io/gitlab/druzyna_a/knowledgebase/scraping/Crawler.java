package io.gitlab.druzyna_a.knowledgebase.scraping;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private static final List<String> visitedPages = Collections.synchronizedList(new LinkedList<String>());
    private static ExecutorService executorService;
    private final String baseUrl;
    private final ScrapeCommand scrapeCommand;
    private final int pagesDepth;
    private boolean main;

    public Crawler(String baseUrl, ScrapeCommand scrapeCommand) {
        this(baseUrl, scrapeCommand, PAGES_DEPTH);
        visitedPages.clear();
        main = true;
        executorService = Executors.newFixedThreadPool(THREAD_POOL);
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
            final Connection connection = Jsoup.connect(baseUrl).timeout(10 * 1000).userAgent(USER_AGENT);
            final Document doc = connection.get();
            System.out.println("Im at " + pagesDepth + " depth on: " + doc.baseUri());
            scrapeCommand.scrape(doc);
            if (pagesDepth == 0) {
                return;
            }
            doc.select("a[href]").stream().filter(l -> !l.attr("href").equals("#") && !visitedPages.contains(l.absUrl(baseUrl))).forEach(l -> {
                final String url = l.absUrl("href");
                visitedPages.add(url);
                executorService.execute(new Crawler(url, scrapeCommand, pagesDepth - 1));
            });
            if (main) {
                try {
                    while (!executorService.awaitTermination(5, TimeUnit.SECONDS));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
