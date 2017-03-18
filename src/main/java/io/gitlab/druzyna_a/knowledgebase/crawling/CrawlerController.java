package io.gitlab.druzyna_a.knowledgebase.crawling;

import io.gitlab.druzyna_a.knowledgebase.model.offered.ArticlesRequest.Article;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
public class CrawlerController {

    private final Crawler[] crawlers = new Crawler[2];
    private ScrapeCommand scrapeCommand;
    private List<Article> articles = Collections.synchronizedList(new LinkedList<>());


    public CrawlerController() {

//        crawlers[0] = new Crawler("https://en.wikipedia.org/wiki/Index_of_fishing_articles",);
    }

    public void startScraping(List<String> tags, int requiredTagsCount) {
        scrapeCommand = new SmartScrapeCommand() {
            @Override
            public void scrape(Document document) {
                Elements divs = document.select("div");
                Elements innerMostDivs = new Elements();
                divs.stream().filter(div -> div.select(">div").isEmpty()).forEach(div -> innerMostDivs.add(div));;
                final List<Element> resultDivs = innerMostDivs.stream().filter(div -> {
                    return tags.stream().filter(tag -> div.text().matches(".*" + tag + ".*")).count() >= requiredTagsCount;
                }).collect(Collectors.toList());
                resultDivs.forEach(div -> {
                    Article article = new Article();
                    article.setTitle(document.title());
                    article.setAuthor(document.baseUri());
                    article.setDescription(div.text());
                });
            }
        };
    }

}
