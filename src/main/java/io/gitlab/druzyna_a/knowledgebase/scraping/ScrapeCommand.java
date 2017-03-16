package io.gitlab.druzyna_a.knowledgebase.scraping;

import org.jsoup.nodes.Document;

/**
 *
 * @author Damian Terlecki
 */
public interface ScrapeCommand {
    void scrape(Document document);
    boolean ignore(String url);
}
