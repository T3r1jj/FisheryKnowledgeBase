package io.gitlab.druzyna_a.knowledgebase.model;

/**
 *
 * @author Damian Terlecki
 */
public class Coordinate {

    /**
     * x coordinate in degrees
     */
    public final double lng;
    /**
     * y coordinate in degrees
     */
    public final double lat;

    public Coordinate(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }
}
