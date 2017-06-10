package io.gitlab.druzyna_a.fisheryknowledgebase.model.offered;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(value = "FishName", description = "FishName resource representation")
public class FishName {

    @ApiModelProperty(value = "Fish name", required = true, example = "northern pike")
    private String name;
    @ApiModelProperty(value = "Fish scientific name", example = "Perca fluviatilis")
    private String sciName;
    @ApiModelProperty(value = "Fish local name", example = "Okoń")
    private String localName;

    public FishName(String name, String sciName, String localName) {
        this.name = name;
        this.sciName = sciName;
        this.localName = localName;
    }

    public String getName() {
        return name;
    }

    public String getSciName() {
        return sciName;
    }

    public String getLocalName() {
        return localName;
    }

}
