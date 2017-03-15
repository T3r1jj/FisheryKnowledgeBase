package io.gitlab.druzyna_a.knowledgebase.scraping;

import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingFishTest {

    private static final int TIMEOUT_SEC = 10;
    private String fishBaseUrl = "http://www.fishbase.org/ComNames/";
    private String fishName;

    @Before
    public void setUp() {
        fishName = "Perch";
    }

    @Test
    public void getFishBaseFish() throws IOException {
        String url = getFishBaseFishLink();
        if (url != null) {
            ScrapingFishTest.this.getFishBaseFish(url);
        }
    }

    private String getFishBaseFishLink() {
        try {
            Connection connection = Jsoup.connect(fishBaseUrl + "CommonNameSearchList.php")
                    .data("CommonName", fishName).timeout(TIMEOUT_SEC * 1000);
            Document doc = connection.post();
            Element content = doc.body();
            Elements links = content.getElementsByTag("a");
            final List<Element> fishLinks = links.stream().filter(l -> l.attr("href").contains("SpeciesSummary.php")).collect(Collectors.toList());
            return fishLinks.get(0).attr("href");
        } catch (Exception e) {
            return null;
        }
    }

    private String getWikipediaFishLink() throws IOException {
        return "https://en.wikipedia.org/wiki/";
    }

    private void getFishBaseFish(String url) {
        System.out.println("Connecting to: " + fishBaseUrl + url);
        Connection connection = Jsoup.connect(fishBaseUrl + url).timeout(TIMEOUT_SEC * 1000);
        try {
            Document doc = connection.get();
            Fish fish = new Fish(fishName);
            final Element sciName = doc.getElementById("ss-sciname").getElementsByClass("sciname").get(0);
            fish.setSciName(getDivSpanContent(sciName));
            final Element main = doc.getElementById("ss-main");
            final Elements divs = main.getElementsByTag("div");
            final Element maturity = divs.get(4);
            float length;
            try {
                length = Float.parseFloat(maturity.text().split(" ")[2].replace(',', '\0'));
                fish.setLength(length / 100f);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            final Element description = divs.get(5);
            fish.setDescription(getDivSpanContent(description));
            final Element biology = divs.get(7);
            fish.setBiology(getDivSpanContent(biology));
            final Element estimation = main.getElementsByClass("smallSpace").select("div:contains(length-weight)").get(0).child(1);
            final String[] splits = estimation.text().split("=");
            float a, b;
            try {
                a = Float.parseFloat(splits[1].split(" ")[0]);
                b = Float.parseFloat(splits[2].split(" ")[0]);
                fish.setWeight(a, b);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ScrapingFishTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDivSpanContent(Element element) {
        element.getElementsByTag("a").forEach(e -> {
            if (e.toString().contains("Ref")) {
                e.remove();
            } else {
                e.unwrap();
            }
        });
        final Element span = element.getElementsByTag("span").get(0);
        return span.text().replace(" (Ref. )", "");
    }
}
