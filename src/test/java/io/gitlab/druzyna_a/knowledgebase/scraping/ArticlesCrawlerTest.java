package io.gitlab.druzyna_a.knowledgebase.scraping;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ArticlesCrawlerTest {

    private final String baseUrl = "http://www.thefishsite.com/articles/";

    @Test
    public void crawlArticles() throws InterruptedException {
        final Thread thread = new Thread(new Crawler(baseUrl, new ScrapeCommand() {
            @Override
            public void scrape(Document document) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ArticlesCrawlerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
        thread.start();
        thread.join();
    }
}
