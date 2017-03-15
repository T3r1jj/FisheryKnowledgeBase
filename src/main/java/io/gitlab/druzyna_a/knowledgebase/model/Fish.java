package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(value = "Fish", description = "Fish resource representation")
public class Fish {

    @ApiModelProperty(value = "Fish name", required = true, example = "northern pike")
    private String name;
    @ApiModelProperty(value = "Fish scientific name", example = "Perca fluviatilis")
    private String sciName;
    @ApiModelProperty(value = "Fish description", example = "Magnificent perch")
    private String description;
    @ApiModelProperty(value = "Interesting information about biological aspects", example = "Most abundant in nutrient-rich lakes and large to medium sized rivers and backwaters")
    private String biology;
    @ApiModelProperty(value = "Average fish weight [kg]", example = "2")
    private float weight;
    @ApiModelProperty(value = "Average fish length [m]", example = "0.2")
    private float length;

    public Fish() {
    }

    public Fish(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    /**
     * Sets weight based on W = aL^b formula, length must be set first
     *
     * @param a for (cm, g)
     * @param b for (cm, g)
     */
    public void setWeight(float a, float b) {
        weight = (float) (a * Math.pow((length / 100f), b) / 1000f);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBiology() {
        return biology;
    }

    public void setBiology(String biology) {
        this.biology = biology;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

}
