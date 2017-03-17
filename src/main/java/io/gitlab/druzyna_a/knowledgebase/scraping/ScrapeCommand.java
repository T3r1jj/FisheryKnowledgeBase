package io.gitlab.druzyna_a.knowledgebase.scraping;

import org.jsoup.nodes.Document;

/**
 *
 * @author Damian Terlecki
 */
public interface ScrapeCommand {
    void scrape(Document document);

    /**
     * Filter accessed pages. Think of: 
     * 1. Black list of webpages 
     * 2. Skipping already visited pages
     * 3. Delay when accessing same host to not put too much load on the web
     *
     * @param url i.e. "https://google.com"
     * @return true if should visit, false if the url should be skipped skip
     */
    boolean shouldVisit(String url);
}
