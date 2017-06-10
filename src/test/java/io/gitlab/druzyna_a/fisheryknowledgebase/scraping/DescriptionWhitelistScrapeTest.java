package io.gitlab.druzyna_a.fisheryknowledgebase.scraping;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class DescriptionWhitelistScrapeTest {

    private static final String RAW_WIKI_HTML = "<div><p><b>Fishing</b> is the activity of trying to catch <a href=\"/wiki/Fish\" title=\"Fish\">fish</a>. Fish are normally caught <a href=\"/wiki/Wild_fisheries\" title=\"Wild fisheries\">in the wild</a>. <a href=\"/wiki/Fishing_techniques\" title=\"Fishing techniques\">Techniques</a> for catching fish include <a href=\"/wiki/Gathering_seafood_by_hand\" title=\"Gathering seafood by hand\">hand gathering</a>, <a href=\"/wiki/Spearfishing\" title=\"Spearfishing\">spearing</a>, <a href=\"/wiki/Fish_net\" class=\"mw-redirect\" title=\"Fish net\">netting</a>, <a href=\"/wiki/Angling\" title=\"Angling\">angling</a> and <a href=\"/wiki/Fish_trap\" title=\"Fish trap\">trapping</a>. Fishing may include catching <a href=\"/wiki/Aquatic_animal\" title=\"Aquatic animal\">aquatic animals</a> other than fish, such as <a href=\"/wiki/Mollusc\" class=\"mw-redirect\" title=\"Mollusc\">molluscs</a>, <a href=\"/wiki/Cephalopod\" title=\"Cephalopod\">cephalopods</a>, <a href=\"/wiki/Crustacean\" title=\"Crustacean\">crustaceans</a>, and <a href=\"/wiki/Echinoderm\" title=\"Echinoderm\">echinoderms</a>. The term is not normally applied to catching <a href=\"/wiki/Fish_farm\" class=\"mw-redirect\" title=\"Fish farm\">farmed fish</a>, or to <a href=\"/wiki/Aquatic_mammal\" title=\"Aquatic mammal\">aquatic mammals</a>, such as whales where the term <a href=\"/wiki/Whaling\" title=\"Whaling\">whaling</a> is more appropriate.</p>\n"
            + "<p>According to United Nations <a href=\"/wiki/Food_and_Agriculture_Organization\" title=\"Food and Agriculture Organization\">FAO</a> statistics, the total number of <a href=\"/wiki/Commercial_fishing\" title=\"Commercial fishing\">commercial fishermen</a> and <a href=\"/wiki/Fish_farming\" title=\"Fish farming\">fish farmers</a> is estimated to be 38 million. <a href=\"/wiki/Fisheries\" class=\"mw-redirect\" title=\"Fisheries\">Fisheries</a> and <a href=\"/wiki/Aquaculture\" title=\"Aquaculture\">aquaculture</a> provide direct and indirect employment to over 500 million people in developing countries.<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[1]</a></sup> In 2005, the worldwide per capita consumption of fish captured from <a href=\"/wiki/Wild_fisheries\" title=\"Wild fisheries\">wild fisheries</a> was 14.4 kilograms, with an additional 7.4 kilograms harvested from fish farms.<sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup> In addition to providing food, modern fishing is also a <a href=\"/wiki/Recreational_fishing\" title=\"Recreational fishing\">recreational pastime</a>.</p>\n"
            + "<p><img alt=\"\" src=\"//upload.wikimedia.org/wikipedia/commons/thumb/1/11/P%C3%A1tzcuaro-Trad-Fishing-3.jpg/300px-P%C3%A1tzcuaro-Trad-Fishing-3.jpg\" width=\"300\" height=\"224\" srcset=\"//upload.wikimedia.org/wikipedia/commons/thumb/1/11/P%C3%A1tzcuaro-Trad-Fishing-3.jpg/450px-P%C3%A1tzcuaro-Trad-Fishing-3.jpg 1.5x, //upload.wikimedia.org/wikipedia/commons/thumb/1/11/P%C3%A1tzcuaro-Trad-Fishing-3.jpg/600px-P%C3%A1tzcuaro-Trad-Fishing-3.jpg 2x\" data-file-width=\"800\" data-file-height=\"596\"></p></div>";
    private static TestData testData;

    @BeforeClass
    public static void setUp() {
        testData = new TestData();
        System.out.println("----------- BEFORE -----------");
        System.out.println(testData.div.toString());
        System.out.println("----------- AFTER -----------");
        System.out.println(testData.cleanedHtml);
    }

    @Test
    public void testDescriptionWhitelistHref() {
        Assert.assertTrue("<a> should contain href", testData.cleanedHtml.contains("href"));
    }

    @Test
    public void testDescriptionWhitelistImg() {
        Assert.assertTrue("<img> should contain src", testData.cleanedHtml.contains("href"));
    }

    @Test
    public void testAbsoluteLinks() {
        Assert.assertTrue("Should contain absolute url", testData.cleanedHtml.contains("href=\"https:"));
    }

    private static class TestData {

        Document div = Jsoup.parse(RAW_WIKI_HTML);
        Whitelist whitelist;
        String cleanedHtml;

        private TestData() {
            div.setBaseUri("https://BASEURL.nice");
            prepareWhitelist();
            prepareDiv();
            clean();
        }

        private void prepareDiv() {
            div.getElementsByTag("a").stream().filter(a -> a.attr("href").contains("edit")).forEach(a -> a.remove());
            div.select("a[href]").stream().forEach(url -> url.attr("href", url.absUrl("href")));
            div.select("img").stream().forEach(img -> img.attr("src", img.absUrl("src")));
            div.select("iframe").stream().forEach(img -> img.attr("src", img.absUrl("src")));
        }

        private void prepareWhitelist() {
            whitelist = Whitelist.relaxed();
            whitelist.addAttributes(":all", "style");
            whitelist.addAttributes("a", "href");
            whitelist.addAttributes("img", "src");
            whitelist.addAttributes("iframe", "src");
        }

        private void clean() {
            cleanedHtml = Jsoup.clean(div.toString(), whitelist);
        }
    }
}
