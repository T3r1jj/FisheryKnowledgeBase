package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(description = "Fisherman equipment")
public class Equipment {

    private String hook = "fly hook";
    private String line = "6kg";
    private String fishingFloat = "avon";
    private String bait = "worms";
    private String lure = "plug";
    private String rod = "ice rod";

    public String getHook() {
        return hook;
    }

    public void setHook(String hook) {
        this.hook = hook;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFishingFloat() {
        return fishingFloat;
    }

    public void setFishingFloat(String fishingFloat) {
        this.fishingFloat = fishingFloat;
    }

    public String getBait() {
        return bait;
    }

    public void setBait(String bait) {
        this.bait = bait;
    }

    public String getRod() {
        return rod;
    }

    public void setRod(String rod) {
        this.rod = rod;
    }

    public String getLure() {
        return lure;
    }

    public void setLure(String lure) {
        this.lure = lure;
    }
    
}
