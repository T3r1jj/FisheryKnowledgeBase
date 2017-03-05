package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(value = "Fish", description = "Fish resource representation")
public class Fish {

    @ApiModelProperty(value = "Fish name", required = true)
    private String name = "Stub name";
    private String description = "A longer description of the fish...";
    @ApiModelProperty(value = "Average fish weight [kg]", example = "2")
    private float weight = 2;
    @ApiModelProperty(value = "Average fish length [m]", example = "0.2")
    private float length = 0.2f;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
