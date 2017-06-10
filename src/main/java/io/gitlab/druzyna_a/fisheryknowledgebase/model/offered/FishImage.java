package io.gitlab.druzyna_a.fisheryknowledgebase.model.offered;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(value = "FishImage", description = "FishImage resource representation")
public class FishImage {

    @ApiModelProperty(value = "Fish url", required = true, example = "http://...")
    private String url;
    @ApiModelProperty(value = "Copyright", required = true, example = "Jan Nowak @ http://...")
    private String author;

    public FishImage(String url, String author) {
        this.url = url;
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
