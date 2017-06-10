package io.gitlab.druzyna_a.fisheryknowledgebase.scraping;

import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.FishName;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.utils.IsoUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ScrapingFishNamesTest {

    private String baseUrl = "http://www.fishbase.org/Country/";
    private String countryCode = "PL";

    @Test
    public void getFishNames() {
        if (!IsoUtil.isValidCountry(countryCode)) {
            return;
        }
        int countryCode = IsoUtil.getCountryCode(this.countryCode);
        List<FishName> fishNames = new LinkedList<FishName>();
        try {
            int page = 1;
            Document doc;
            do {
                Connection connection = Jsoup.connect(baseUrl + "CountryChecklist.php?c_code=" + countryCode + "&vhabitat=all2&csub_code=&cpresence=present&resultPage=" + page);
                doc = connection.get();
                Element content = doc.body();
                Element table = doc.select("table[class=commonTable]").first();
                Iterator<Element> it = table.select("td").iterator();
                while (it.hasNext()) {
                    it.next();
                    it.next();
                    String species = it.next().text();
                    it.next();
                    String name = it.next().text();
                    String localName = it.next().text();
                    fishNames.add(new FishName(name, species, localName));
                }
                page++;
            } while (doc.getElementsByTag("a").text().contains("Next"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
