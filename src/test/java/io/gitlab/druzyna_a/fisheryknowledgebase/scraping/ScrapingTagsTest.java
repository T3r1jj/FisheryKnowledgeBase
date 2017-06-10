package io.gitlab.druzyna_a.fisheryknowledgebase.scraping;

import io.gitlab.druzyna_a.fisheryknowledgebase.crawling.Crawler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingTagsTest {

    private String[] baseUrls = new String[]{
        "http://www.tackledirect.com/",
        "http://fish.shimano.com/content/sac-fish/en/home/products.html"
    };
    private String fishingBaseUrl = "https://en.wikipedia.org/wiki/Index_of_fishing_articles";

    private List<List<String>> tags = new ArrayList<List<String>>();
    private List<String> fishingTags = new LinkedList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Before
    public void setUp() {
        for (int i = 0; i < Tags.values().length; i++) {
            tags.add(Collections.synchronizedList(new LinkedList<>()));
        }
    }

    @Ignore
    @Test
    public void getAllTags() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(baseUrls[0]).timeout(10 * 1000);
                try {
                    Document doc = connection.get();
                    Arrays.asList(Tags.values()).stream().forEach(tt -> doc.getElementsByAttributeValue("href", tt.toString()).get(0).parent().siblingElements().stream().forEach(t -> tags.get(tt.ordinal()).add(t.text())));
                } catch (IOException ex) {
                    Logger.getLogger(ScrapingTagsTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(baseUrls[1]).timeout(10 * 1000);
                try {
                    Document doc = connection.get();
                    Arrays.asList(Tags2.values()).stream().forEach(tt -> doc.getElementsByClass("nav-col").get(tt.ordinal).getElementsByTag("a").stream().forEach(t -> tags.get(tt.getTrueOrdinal()).add(t.text())));
                } catch (IOException ex) {
                    Logger.getLogger(ScrapingTagsTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(100, TimeUnit.MILLISECONDS));
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        executorService.shutdown();
        tags.forEach(l -> System.out.println(l));
    }

    @Test
    public void getFishingTags() {
        Connection connection = Jsoup.connect(fishingBaseUrl).timeout(10 * 1000);
        try {
            Document doc = connection.get();
            doc.getElementsByTag("p").stream().forEach(p -> p.getElementsByTag("a").stream().forEach(a -> fishingTags.add(a.text())));
        } catch (IOException ex) {
            Logger.getLogger(ScrapingTagsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fishingTags);
    }

    enum Tags {
        REELS, RODS, LURES1, TERMINAL, SALTWATER_FISHING_ACCESSORIES;

        @Override
        public String toString() {
            return name().toLowerCase().replace("_", "-") + ".html";
        }

    }

    enum Tags2 {
        FISHING_REELS(0), FISHING_RODS(1), CLOTHING(4), GEAR(4), LURES(2);
        private final int ordinal;

        private Tags2(int ordinal) {
            this.ordinal = ordinal;
        }

        public int getTrueOrdinal() {
            return ordinal;
        }

    }
}
