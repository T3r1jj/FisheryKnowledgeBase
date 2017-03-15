package io.gitlab.druzyna_a.knowledgebase.scraping;

import java.net.URLEncoder;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingFishOccurenceTest {

    private final String fish = "perch";
    private double lat = 52;
    private double lng = 23;
    private double lat2 = 53;
    private double lng2 = 24;
    private final String baseUrl = "http://gbif.org/occurrence/search";

    @Test
    public void exists() {
        String params = URLEncoder.encode(lng + " " + lat + "," + lng + " " + lat2 + "," + lng2 + " " + lat2 + "," + lng2 + " " + lat + "," + lng + " " + lat);
        Connection connection = Jsoup.connect(baseUrl + "?q=" + fish + "&GEOMETRY=" + params);
        System.out.println(baseUrl + "?q=" + fish + "&GEOMETRY=" + params);
        try {
            Document doc = connection.get();
            final String resultsText = doc.getElementsByClass("results").text();
            if (resultsText.contains("results") && !resultsText.contains("0 results")) {
                return;
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}
