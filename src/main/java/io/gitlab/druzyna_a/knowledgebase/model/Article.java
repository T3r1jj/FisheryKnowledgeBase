package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel
public class Article {

    @ApiModelProperty
    private String title = "Metody i techniki wędkowania";
    @ApiModelProperty
    private String description = "Należy wyraźnie odróżniać definicję metody od techniki wędkowania. Przez wiele lat, a właściwie do dzisiaj, obydwa te pojęcia są w potocznym języku wędkarzy mylone i stosowane zamiennie.\n"
            + "\n"
            + "Dopiero po 1989 r. większe otwarcie na światową literaturę wędkarską (w której profesjonalna nomenklatura tematu była już ukształtowana) oraz nastanie wolności wydawniczej spowodowały ogromne, w stosunku do poprzednich lat, zwiększenie nakładów i tytułów wydawnictw wędkarskich, nie tylko ogólnotematycznych, ale także wszelkiego rodzaju monografii i pozycji zajmujących się wieloma szczegółowymi aspektami wędkarstwa. Na tej podstawie ujednoliciły się pojęcia i definicje dotyczące teorii wędkowania. Ustaliły się pewne istotne reguły w tym zakresie (chociaż niektórym autorom, od czasu do czasu, nadal myli się nazewnictwo).";
    @ApiModelProperty
    private String author = "https://pl.wikipedia.org/wiki/W%C4%99dkarstwo Creative Commons Attribution-ShareAlike 3.0 Unported License";

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
