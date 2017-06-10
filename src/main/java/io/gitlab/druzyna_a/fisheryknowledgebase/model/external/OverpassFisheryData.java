package io.gitlab.druzyna_a.fisheryknowledgebase.model.external;

import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Coordinate;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Fishery;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Damian Terlecki
 */
public class OverpassFisheryData {

    public static final String LICENSE = "The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.";
    private Osm3s meta;
    private OverpassFishery[] elements;

    public static class OverpassFishery {

        private double lat;
        private double lon;
        private Tags tags;
    }

    public static class Tags {

        private String leisure;
        private String name;
        private String shop;
        private String fishing;//private
        private String website;
    }

    public static class Osm3s {

        private String copyright;
    }

    public String getLicense() {
        return meta.copyright;
    }

    public List<Fishery> toFisheries() {
        List<Fishery> fisheries = new LinkedList<>();
        for (OverpassFishery element : elements) {
            if (element.tags.shop == null) {
                Fishery fishery = new Fishery(new Coordinate(element.lat, element.lon));
                fishery.setName(element.tags.name);
                fishery.setRequirements(element.tags.fishing);
                fishery.setDescription(element.tags.website);
                fisheries.add(fishery);
            }
        }
        return fisheries;
    }
}
