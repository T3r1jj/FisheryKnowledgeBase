package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel(description = "Basic information about fishery place")
public class Fishery {
    @ApiModelProperty
    private Coordinate coordinate = new Coordinate(23.0294058, 53.8824905);
    @ApiModelProperty
    private String name = "kalejty";
    @ApiModelProperty
    private String description = "longer description";
    @ApiModelProperty
    private String requirements = "Warunki wędkowania na jeziorze Długie Augustowskie (Kalejty) w zlewni rzeki Rospuda nr 14.\n"
            + "-------------------------------------------------------------------------------------------\n"
            + "Zezwolenie jest ważne jedynie z kartą wędkarską i wniesionym i składkami członkowskimi Okręgu PZW w Białymstoku\n"
            + "lub wniesionymi opłatami za wędkowanie dla osób niezrzeszonych w PZW.\n"
            + " \n"
            + "Amatorski połów ryb odbywa się w sposób i na zasadach określonych w Regulaminie Amatorskiego Połowu Ryb uchwalonego przez Zarząd Główny PZW z ograniczeniami:\n"
            + "jednocześnie może wędkować maksymalnie 20 osób, w tym z 5 jednostek pływających bez silników spalinowych\n"
            + "połów ryb może odbywać się jedynie z pomostów oznakowanych (5 sztuk), w rejonie wsi Strękowizna,\n"
            + "zakaz wypuszczania złowionych ryb karpiowatych, zachowując wymiary i okresy ochronne,\n"
            + "łączna liczba złowionych i zabieranych ryb (szczupak szt. 2,węgorz szt. 2, sum szt. 1, sandacz szt. 2, lin szt. 4) z łowiska nie może przekroczyć 10 sztuk w ciągu doby.";

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
}
