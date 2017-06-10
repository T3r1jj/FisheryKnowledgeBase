package io.gitlab.druzyna_a.fisheryknowledgebase.scraping;

import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Coordinate;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Fish;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.FishImage;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.FishName;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.FishProtection;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.external.BoundingBox;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.utils.IsoUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Damian Terlecki
 */
@Component
@Scope("singleton")
public class FishScraper {

    private static final int TIMEOUT_SEC = 20;

    public Optional<List<FishName>> scrapeFishNames(String countryCode) throws IOException {
        if (!IsoUtil.isValidCountry(countryCode)) {
            return Optional.empty();
        }
        int numericCountryCode = IsoUtil.getCountryCode(countryCode);
        List<FishName> fishNames = new LinkedList<>();
        String url = BaseUrls.FISH_NAMES + "CountryChecklist.php?c_code=" + numericCountryCode + "&vhabitat=all2&csub_code=&cpresence=present&showAll=yes";
        Connection connection = Jsoup.connect(url).timeout(TIMEOUT_SEC * 1000);
        Document doc = connection.get();
        Element content = doc.body();
        Element table = doc.select("table[class=commonTable]").first();
        Iterator<Element> it = table.select("td").iterator();
        while (it.hasNext()) {
            it.next();
            it.next();
            String species = it.next().text().replace("\u00a0", "");
            it.next();
            String name = it.next().text().replace("\u00a0", "");
            String localName = it.next().text().replace("\u00a0", "");
            fishNames.add(new FishName(name, species, localName));
        }
        return Optional.of(fishNames);
    }

    public Optional<Fish> scrapeCommonFish(String fishName) throws IOException {
        String url = scrapeFishBaseFishLink(fishName);
        if (url == null) {
            return Optional.empty();
        } else {
            return Optional.of(scrapeFishBaseFish(url, fishName));
        }
    }

    private String scrapeFishBaseFishLink(String fishName) throws IOException {
        Connection connection = Jsoup.connect(BaseUrls.FISH + "CommonNameSearchList.php")
                .data("CommonName", fishName).timeout(TIMEOUT_SEC * 500).followRedirects(true);
        Document doc = connection.post();
        Element content = doc.body();
        Elements links = content.getElementsByTag("a");
        List<Element> fishLinks = links.stream().filter(l -> l.attr("href").contains("SpeciesSummary.php")).collect(Collectors.toList());
        if (fishLinks.isEmpty()) {
            return null;
        }
        return fishLinks.get(0).attr("href");
    }

    private Fish scrapeFishBaseFish(String url, String fishName) throws IOException {
        Connection connection = Jsoup.connect(BaseUrls.FISH + url).timeout(TIMEOUT_SEC * 500);
        return scrapeFishBaseFishContent(connection, fishName);
    }

    private Fish scrapeFishBaseFishContent(Connection connection, String fishName) throws IOException {
        Document doc = connection.get();
        Element commonName = doc.getElementById("ss-sciname").getElementsByClass("sheader2").get(0);
        Fish fish = new Fish(getDivSpanContent(commonName));
        Element sciName = doc.getElementById("ss-sciname").getElementsByClass("sciname").get(0);
        fish.setSciName(getDivSpanContent(sciName));
        Element main = doc.getElementById("ss-main");
        Elements divs = main.getElementsByTag("div");
        Element maturity = divs.get(4);
        float length;
        try {
            length = Float.parseFloat(maturity.text().split(" ")[2].replace(',', '\0'));
            fish.setLength(length / 100f);
        } catch (NumberFormatException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        Element description = divs.get(5);
        fish.setDescription(getDivSpanContent(description));
        Element biology = divs.get(7);
        fish.setBiology(getDivSpanContent(biology));
        Element estimation = main.getElementsByClass("smallSpace").select("div:contains(length-weight)").get(0).child(1);
        String[] splits = estimation.text().split("=");
        float a, b;
        try {
            a = Float.parseFloat(splits[1].split(" ")[0]);
            b = Float.parseFloat(splits[2].split(" ")[0]);
            fish.setWeight(a, b);
        } catch (NumberFormatException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return fish;
    }

    private String getDivSpanContent(Element element) {
        element.getElementsByTag("a").forEach(e -> {
            if (e.toString().contains("Ref")) {
                e.remove();
            } else {
                e.unwrap();
            }
        });
        Element span = element.getElementsByTag("span").get(0);
        return span.text().replace(" (Ref. )", "");
    }

    public Optional<FishProtection> scrapeFishProtection(String fishSciName) throws IOException {
        String protectionUrl = getProtectionUrl(fishSciName);
        if (protectionUrl != null) {
            Connection connection = Jsoup.connect(BaseUrls.FISH_PROTECTION + protectionUrl).timeout(TIMEOUT_SEC * 500);
            Document doc = connection.get();
            FishProtection protection = new FishProtection();
            Element status = doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Red List Category & Criteria")).findFirst().get().parent().child(1);
            status.getElementsByTag("a").remove();
            protection.setStatus(status.text());
            Element assessment = doc.getElementsByTag("td").stream().filter(td -> td.text().contains("Justification:")).findFirst().get();
            protection.setAssessment(assessment.text());
            doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Use and Trade:")).findFirst().ifPresent(l -> protection.setUseAndTrade(l.parent().child(1).text()));
            Element conservation = doc.getElementsByClass("label").stream().filter(l -> l.text().contains("Conservation Actions:")).findFirst().get().parent().child(1);
            protection.setConservation(conservation.text());
            protection.setCopyright(doc.baseUri());
            return Optional.of(protection);
        }
        return null;
    }

    private String getProtectionUrl(String fishSciName) throws IOException {
        Connection connection = Jsoup.connect(BaseUrls.FISH_PROTECTION + "/search/external").timeout(TIMEOUT_SEC * 500).data("text", fishSciName);
        Document doc = connection.post();
        Element results = doc.getElementById("results");
        if (results.childNodeSize() > 0) {
            return results.child(0).getElementsByClass("title").attr("href");
        }
        return null;
    }

    public boolean scrapeExists(List<Coordinate> coordinates, String fishName) throws IOException {
        BoundingBox bbox = new BoundingBox();
        coordinates.stream().forEach(c -> bbox.add(c));
        try {
            String params = URLEncoder.encode(bbox.getMinLng() + " " + bbox.getMinLat() + ","
                    + bbox.getMinLng() + " " + bbox.getMaxLat() + ","
                    + bbox.getMaxLng() + " " + bbox.getMaxLat() + ","
                    + bbox.getMaxLng() + " " + bbox.getMinLat() + ","
                    + bbox.getMinLat() + " " + bbox.getMinLng(), "utf8");
            Connection connection = Jsoup.connect(BaseUrls.FISH_OCCURENCE + "?q=" + fishName + "&GEOMETRY=" + params).timeout(TIMEOUT_SEC * 1000);
            Document doc = connection.get();
            String resultsText = doc.getElementsByClass("results").text();
            return resultsText.contains("results") && !resultsText.contains("0 results");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FishScraper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public List<FishImage> scrapeImages(String fishSciName) throws IOException {
        String[] genusSpecies = fishSciName.split(" ");
        if (genusSpecies.length < 2) {
            return new LinkedList<>();
        }
        Connection connection = Jsoup.connect(BaseUrls.FISH_IMAGES + "?Genus=" + genusSpecies[0] + "&Species=" + genusSpecies[1]).timeout(TIMEOUT_SEC * 1000);
        List<FishImage> images = new LinkedList<>();
        Document doc = connection.get();
        Elements divs = doc.getElementsByTag("td");
        divs.stream().filter(e -> e.text().contains("CC-BY")).forEach(e -> {
            String src = e.getElementsByTag("img").get(0).attr("src");
            try {
                FishImage image = new FishImage(getRawUrl(src), e.getElementsByAttributeValue("href", "#").toString());
                images.add(image);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        });
        return images;
    }

    private String getRawUrl(String src) throws UnsupportedEncodingException {
        return URLDecoder.decode(src, "utf8").replace("workimagethumb.php?s=", "").replace("&w=300", "");
    }

    public Optional<Fish> scrapeScientificFish(String name) throws IOException {
        try {
            String[] genusSpecies = name.split(" ");
            Connection connection = Jsoup.connect(BaseUrls.FISH_SCI + genusSpecies[0] + "-" + genusSpecies[1] + ".html").timeout(TIMEOUT_SEC * 1000);
            try {
                return Optional.of(scrapeFishBaseFishContent(connection, name));
            } catch (NullPointerException nullPointerException) {
                connection = Jsoup.connect(BaseUrls.FISH_SCI_BACKUP + genusSpecies[0] + "-" + genusSpecies[1] + ".html").timeout(TIMEOUT_SEC * 1000);
                return Optional.of(scrapeFishBaseFishContent(connection, name));
            }
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    private enum BaseUrls {
        FISH_NAMES("http://www.fishbase.org/Country/"), FISH("http://www.fishbase.org/ComNames/"), FISH_SCI("http://www.fishbase.org/summary/"), FISH_SCI_BACKUP("http://www.fishbase.de/summary/"),
        FISH_PROTECTION("http://www.iucnredlist.org"), FISH_OCCURENCE("http://gbif.org/occurrence/search"),
        FISH_IMAGES("http://www.fishbase.org/photos/thumbnailssummary.php");

        private final String url;

        private BaseUrls(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return this.url;
        }

    }
}
