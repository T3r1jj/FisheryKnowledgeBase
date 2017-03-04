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
    @ApiModelProperty(value = "Weight")
    private double weight = 9000;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    
}
