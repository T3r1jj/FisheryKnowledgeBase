package io.gitlab.druzyna_a.knowledgebase.scraping;

import io.gitlab.druzyna_a.knowledgebase.crawling.Crawler;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Damian Terlecki
 */
@Component
@Scope("singleton")
public class TagsScraper {

    private static final int TIMEOUT_SEC = 10;

    private static final String[] BASE_URLS = new String[]{
        "http://www.tackledirect.com/",
        "http://fish.shimano.com/content/sac-fish/en/home/products.html",
        "http://en.wikipedia.org/wiki/Index_of_fishing_articles"
    };

    public List<String> scrapeTags(Tags tagsType) {
        int ordinal = tagsType.ordinal();
        if (ordinal >= ScrapeTags1.values().length) {
            return scrapeFishingTags();
        } else {
            return scrapeAllTags().get(ordinal);
        }
    }

    private List<List<String>> scrapeAllTags() {
        List<List<String>> tags = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (ScrapeTags1 v : ScrapeTags1.values()) {
            tags.add(Collections.synchronizedList(new LinkedList<>()));
        }
        executorService.execute(() -> {
            Connection connection = Jsoup.connect(BASE_URLS[0]).timeout(10 * 1000);
            try {
                Document doc = connection.get();
                Arrays.asList(ScrapeTags1.values()).stream().forEach(tt -> doc.getElementsByAttributeValue("href", tt.toString()).get(0).parent().siblingElements().stream().forEach(t -> tags.get(tt.ordinal()).add(t.text())));
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        });

        executorService.execute(() -> {
            Connection connection = Jsoup.connect(BASE_URLS[1]).timeout(10 * 1000);
            try {
                Document doc = connection.get();
                Arrays.asList(ScrapeTags2.values()).stream().forEach(tt -> doc.getElementsByClass("nav-col").get(tt.ordinal).getElementsByTag("a").stream().forEach(t -> tags.get(tt.getTrueOrdinal()).add(t.text())));
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        });
        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(100, TimeUnit.MILLISECONDS));
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tags;
    }

    public List<String> scrapeFishingTags() {
        Connection connection = Jsoup.connect(BASE_URLS[2]).timeout(TIMEOUT_SEC * 1000);
        try {
            Document doc = connection.get();
            List<String> fishingTags = new LinkedList<>();
            doc.getElementsByTag("p").stream().forEach(p -> p.getElementsByTag("a").stream().forEach(a -> fishingTags.add(a.text())));
            return fishingTags;
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }

    private enum ScrapeTags1 {
        REELS, RODS, LURES1, TERMINAL, SALTWATER_FISHING_ACCESSORIES;

        @Override
        public String toString() {
            return name().toLowerCase().replace("_", "-") + ".html";
        }

    }

    private enum ScrapeTags2 {
        FISHING_REELS(0), FISHING_RODS(1), CLOTHING(4), GEAR(4), LURES(2);
        private final int ordinal;

        private ScrapeTags2(int ordinal) {
            this.ordinal = ordinal;
        }

        public int getTrueOrdinal() {
            return ordinal;
        }

    }

    public enum Tags {
        REELS, RODS, LURES, TACKLE, ACCESSORIES, FISHING
    }
}
