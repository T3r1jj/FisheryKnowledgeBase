package io.gitlab.druzyna_a.knowledgebase.crawling;

import io.gitlab.druzyna_a.knowledgebase.crawling.SmartScrapeCommand;
import io.gitlab.druzyna_a.knowledgebase.crawling.Crawler;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        "line", "rod", "reel", "spin casting", "heheniematego", "fish"
    });
    private final int minimalTagsInArticle = 3;
    private final List<String> articles = Collections.synchronizedList(new LinkedList<>());

    @Test
    public void crawlArticles() throws InterruptedException {
        final Thread thread = new Thread(new Crawler.Builder()
                .setBaseUrl(baseUrl)
                .setPagesDepth(3)
                .setThreadPool(8)
                .setScrapeCommand(new SmartScrapeCommand() {
                    @Override
                    public void scrape(Document document) {
                        Elements divs = document.select("div");
                        Elements innerMostDivs = new Elements();
                        divs.stream().filter(div -> div.select(">div").isEmpty()).forEach(div -> innerMostDivs.add(div));;
                        final List<Element> resultDivs = innerMostDivs.stream().filter(div -> {
                            return tags.stream().filter(tag -> div.text().matches(".*" + tag + ".*")).count() >= minimalTagsInArticle;
                        }).collect(Collectors.toList());
                        resultDivs.forEach(div -> articles.add(div.text()));
                    }

                }).createCrawler()
        );
        thread.start();
        thread.join(30 * 1000);
        articles.forEach(s -> System.out.println(s + "\n\n"));
    }

}
