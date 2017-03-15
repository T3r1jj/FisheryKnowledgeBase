package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(description = "Protection information about fish in respect of period, weight, length or a combination of those.")
public class Protection {

    
    @ApiModelProperty(value = "Status", example = "Least Concern")
    private String status;
    @ApiModelProperty(value = "Assessment", example = "A widespread species and not globally threatened.")
    private String assessment;
    @ApiModelProperty(value = "Use and trade", example = "Gorillas are completely protected by national and international laws in all countries of their range, and it is, therefore, illegal to kill, capture or trade in live Gorillas or their body parts.")
    private String useAndTrade;
    @ApiModelProperty(value = "Conservation actions", example = "To address the critical situation faced by Grauer’s Gorillas, NGOs are working with the government authorities to support protected areas and reinforce conservation programmes.")
    private String conservation;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getUseAndTrade() {
        return useAndTrade;
    }

    public void setUseAndTrade(String useAndTrade) {
        this.useAndTrade = useAndTrade;
    }

    public String getConservation() {
        return conservation;
    }

    public void setConservation(String conservation) {
        this.conservation = conservation;
    }

}
