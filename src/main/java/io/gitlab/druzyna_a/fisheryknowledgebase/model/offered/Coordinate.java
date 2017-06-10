package io.gitlab.druzyna_a.fisheryknowledgebase.model.offered;

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

    public Coordinate(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }
}
