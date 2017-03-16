package io.gitlab.druzyna_a.knowledgebase.scraping;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class ArticlesCrawlerTest {

    private final String baseUrl = "https://www.google.pl/search?q=spincasting&oq=spincasting";
    private final List<String> tags = Arrays.asList(new String[]{
        "line", "rod", "reel", "spin casting", "dupaniematego", "fish"
    });
    private final int minimalTagsInArticle = 3;
    private final List<String> articles = Collections.synchronizedList(new LinkedList<>());

    @Test
    public void crawlArticles() throws InterruptedException {
        final Thread thread = new Thread(new Crawler(baseUrl, new SmartScrapeCommand() {
            @Override
            public void scrape(Document document) {
                try {
                    Elements divs = document.select("div");
                    Elements innerMostDivs = new Elements();
                    divs.stream().filter(div -> div.select(">div").isEmpty()).forEach(div -> innerMostDivs.add(div));;
                    final List<Element> resultDivs = innerMostDivs.stream().filter(div -> {
                        return tags.stream().filter(tag -> div.text().matches(".*" + tag + ".*")).count() >= minimalTagsInArticle;
                    }).collect(Collectors.toList());
                    resultDivs.forEach(div -> articles.add(div.text()));
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ArticlesCrawlerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }));
        thread.start();
        thread.join();
        articles.forEach(s -> System.out.println(s + "\n\n"));
    }

    public abstract static class SmartScrapeCommand implements ScrapeCommand {

        private final List<String> blackList = Arrays.asList(new String[]{
            "facebook", "twitter", "instagram", ".google", "youtube"
        });

        @Override
        public boolean ignore(String url) {
            if (blackList.stream().filter(i -> url.matches(".*" + i + ".*")).count() > 0) {
                return true;
            }
            return false;
        }
    }
}
