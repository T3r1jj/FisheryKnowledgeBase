package io.gitlab.druzyna_a.knowledgebase.scraping;

import io.gitlab.druzyna_a.knowledgebase.model.offered.FishProtection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingFishProtectionTest {

    private final String fish = "Rhynchocypris percnurus";
    private final String baseUrl = "http://www.iucnredlist.org";

    @Test
    public void getFishProtection() {
        String protectionUrl = getProtectionUrl();
        if (protectionUrl != null) {
            Connection connection = Jsoup.connect(baseUrl + protectionUrl).timeout(10 * 1000);
            try {
                Document doc = connection.get();
                FishProtection protection = new FishProtection();
                final Element status = doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Red List Category & Criteria")).findFirst().get().parent().child(1);
                status.getElementsByTag("a").remove();
                protection.setStatus(status.text());
                final Element assessment = doc.getElementsByTag("td").stream().filter(td -> td.text().contains("Justification:")).findFirst().get();
                protection.setAssessment(assessment.text());
                doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Use and Trade:")).findFirst().ifPresent(l -> protection.setUseAndTrade(l.parent().child(1).text()));
                final Element conservation = doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Conservation Actions:")).findFirst().get().parent().child(1);
                protection.setConservation(conservation.text());
            } catch (IOException ex) {
                Logger.getLogger(ScrapingFishProtectionTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getProtectionUrl() {
        Connection connection = Jsoup.connect(baseUrl + "/search/external").timeout(10 * 1000).data("text", fish);
        try {
            Document doc = connection.post();
            final Element results = doc.getElementById("results");
            if (results.childNodeSize() > 0) {
                return results.child(0).getElementsByClass("title").attr("href");
            } else if (doc.text().contains("No entries found")) {
            }
        } catch (IOException ex) {
            Logger.getLogger(ScrapingFishProtectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
