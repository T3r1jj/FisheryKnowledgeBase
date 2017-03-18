package io.gitlab.druzyna_a.knowledgebase.model.offered;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author Damian Terlecki
 */
public class ArticlesRequest {

    @Id
    private String id;
    private Long time;
    private boolean scraped;
    private List<String> tags;
    private int requiredTagsCount;
    private List<Article> articles;
    private boolean quick;

    public ArticlesRequest() {
    }

    public ArticlesRequest(List<String> tags, int requiredTagsCount) {
        this.tags = tags;
        this.requiredTagsCount = requiredTagsCount;
    }

    public Long getTime() {
        return time;
    }

    private void setTime(Long time) {
        this.time = time;
    }

    public boolean isScraped() {
        return scraped;
    }

    public void setScraped(boolean scraped) {
        this.scraped = scraped;
        setTime(Instant.now().getEpochSecond());
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getRequiredTagsCount() {
        return requiredTagsCount;
    }

    public void setRequiredTagsCount(int requiredTagsCount) {
        this.requiredTagsCount = requiredTagsCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public boolean isQuick() {
        return quick;
    }

    public void setQuick(boolean quick) {
        this.quick = quick;
    }

    @ApiModel
    public static class Article {

        @ApiModelProperty
        private String title;
        @ApiModelProperty
        private String description;
        @ApiModelProperty(value = "Author or scraped site url")
        private String author;
        @ApiModelProperty(value = "Image url")
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void appendDescription(String description) {
            if (isEmpty()) {
                this.description = description;
            } else {
                this.description += "<br/>" + description;
            }
        }

        public boolean isEmpty() {
            return description == null || description.isEmpty();
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

}
