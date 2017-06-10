package io.gitlab.druzyna_a.fisheryknowledgebase.scraping;

import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.FishImage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingFishImagesTest {

    private final String baseUrl = "http://www.fishbase.org/photos/thumbnailssummary.php";
    private final String fish = "perca fluviatilis";

    @Test
    public void getImages() {
        final String[] genusSpecies = fish.split(" ");
        Connection connection = Jsoup.connect(baseUrl + "?Genus=" + genusSpecies[0] + "&Species=" + genusSpecies[1]).timeout(10 * 1000);
        List<FishImage> images = new LinkedList<>();
        try {
            Document doc = connection.get();
            final Elements divs = doc.getElementsByTag("td");
            divs.stream().filter(e -> e.text().contains("CC-BY")).forEach(e -> {
                final String src = e.getElementsByTag("img").get(0).attr("src");
                try {
                    FishImage image = new FishImage(getRawUrl(src), e.getElementsByAttributeValue("href", "#").toString());
                    images.add(image);
                    System.out.println(image.getUrl());
                    System.out.println(image.getAuthor());
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ScrapingFishImagesTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static String getRawUrl(final String src) throws UnsupportedEncodingException {
        return URLDecoder.decode(src, "utf8").replace("workimagethumb.php?s=", "").replace("&w=300", "");
    }
}
