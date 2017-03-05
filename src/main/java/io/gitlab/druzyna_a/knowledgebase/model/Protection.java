package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(description = "Protection information about fish in respect of period, weight, length or a combination of those.")
public class Protection {

    @ApiModelProperty(value = "Fish protection period [dd/MM-dd/MM], none if protection not based on period")
    private Period period = new Period();
    @ApiModelProperty(value = "Fish protection weight [kg] (up to), zero if protection not based on weight", example = "2")
    private float weight = 2;
    @ApiModelProperty(value = "Fish protection length [m] (up to), zero if protection not based on length", example = "0.3")
    private float length = 0.3f;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @ApiModel(description = "Simple period data in dd/MM format (start-end)")
    public class Period {

        @ApiModelProperty(value = "dd/MM", example = "14/02", required = true)
        public final String startDate;
        @ApiModelProperty(value = "dd/MM", example = "14/02", required = true)
        public final String endDate;

        public Period() {
            this("14/02", "15/05");
        }

        public Period(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

    }

}
