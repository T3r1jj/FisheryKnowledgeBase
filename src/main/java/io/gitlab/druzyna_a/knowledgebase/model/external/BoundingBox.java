package io.gitlab.druzyna_a.knowledgebase.model.external;

import io.gitlab.druzyna_a.knowledgebase.model.Coordinate;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Damian Terlecki
 */
public class BoundingBox {

    private double maxLng = Double.NEGATIVE_INFINITY;
    private double minLng = Double.POSITIVE_INFINITY;
    private double maxLat = Double.NEGATIVE_INFINITY;
    private double minLat = Double.POSITIVE_INFINITY;

    public static List<Coordinate> getBBoxCoordinates(double lat, double lng, double radius) {
        double R = 6378137;
        double dLat = radius / R;
        double dLng = radius / (R * Math.cos(Math.PI * lat / 180));
        dLat = dLat * 180 / Math.PI;
        dLng = dLng * 180 / Math.PI;
        return Arrays.asList(new Coordinate[]{
            new Coordinate(lat + dLat, lng + dLng), new Coordinate(lat - dLat, lng + dLng), new Coordinate(lat + dLat, lng - dLng), new Coordinate(lat - dLat, lng - dLng)
        });
    }

    public void add(Coordinate coordinate) {
        maxLng = Math.max(coordinate.lng, maxLng);
        minLng = Math.min(coordinate.lng, minLng);
        maxLat = Math.max(coordinate.lat, maxLat);
        minLat = Math.min(coordinate.lat, minLat);
    }

    /**
     * @return @see {@link Coordinate#lng}
     */
    public double getMaxLng() {
        return maxLng;
    }

    /**
     * @return @see {@link Coordinate#lng}
     */
    public double getMinLng() {
        return minLng;
    }

    /**
     * @return @see {@link Coordinate#lat}
     */
    public double getMaxLat() {
        return maxLat;
    }

    /**
     * @return @see {@link Coordinate#lat}
     */
    public double getMinLat() {
        return minLat;
    }

    @Override
    public String toString() {
        return "(" + minLat + "," + minLng + "," + maxLat + "," + maxLng + ")";
    }
}
