package io.gitlab.druzyna_a.knowledgebase.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 * @author Damian Terlecki
 */
@ApiModel
public class Weather {

    @ApiModelProperty(value = "Time in epoch [s] UTC", required = true)
    public final long time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    /**
     * pressure [Pa]
     */
    @ApiModelProperty(value = "Pressure [Pa]", required = true)
    public final double p;
    /**
     * temperature [K]
     */
    @ApiModelProperty(value = "Temperature [K]", required = true)
    public final double T;
    /**
     * relative humidity [0d..1d]
     */
    @ApiModelProperty(value = "Relative humidity", allowableValues = "range[0, 1]", required = true)
    public final double phi;
    /**
     * [m/s]
     */
    @ApiModelProperty(value = "Wind speed [m/s]", required = true)
    public final double windSpeed;
    /**
     * [rad]
     */
    @ApiModelProperty(value = "Wind angle [rad]", required = true)
    public final double windAngle;

    public Weather() {
        this(101325, 288.15, 0, 0, 0);
    }

    /**
     * @param p air pressure [Pa]
     * @param T temperature [K]
     * @param phi humidity [0d..1d]
     * @param windSpeed [m/s]
     * @param windAngle [rad]
     */
    public Weather(double p, double T, double phi, double windSpeed, double windAngle) {
        this.p = p;
        this.T = T;
        this.phi = phi;
        this.windSpeed = windSpeed;
        this.windAngle = windAngle;
    }

    /**
     * @param kph kilometers per hour [km/h]
     * @return meters per second [m/s]
     */
    public static double kphToMps(double kph) {
        return kph * 1000 / 3600;
    }

    /**
     * @param knots speed [nautical miles / hour]
     * @return [m/s]
     */
    public static double ktToMps(double knots) {
        return kphToMps(ktToKph(knots));
    }

    /**
     * @param knots speed [nautical miles / hour]
     * @return [km/h]
     */
    public static double ktToKph(double knots) {
        return 1.852 * knots;
    }

    /**
     * @param t temperature [K]
     * @return Temperature [*C]
     */
    public static double kelvinToCelsius(double t) {
        return t - 273.15d;
    }

    /**
     * @param T Temperature [*C]
     * @return temperature [K]
     */
    public static double celsiusToKelvin(double T) {
        return T + 273.15d;
    }

    /**
     * @return direction e.g. "N" wind direction is from north to south
     */
    public String getWindDirection() {
        int directionIndex = getWindDirectionIndex();
        String[] directions = new String[]{"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        return directions[directionIndex];
    }

    /**
     * @return 16 indexes of subsequent directions clockwise starting from N
     */
    public int getWindDirectionIndex() {
        return (int) (Math.round((windAngle / (Math.PI / 8))) % 16);
    }

    /**
     * @return empirical measure that relates wind speed to observed conditions
     * at sea or on land
     */
    public int getBeaufortNumber() {
        return (int) Math.round(Math.pow(windSpeed / 0.836, 2d / 3d));
    }

    @Override
    public String toString() {
        return "Weather{"
                + "p=" + p
                + " [Pa], T=" + T
                + " [K], phi=" + phi
                + " [0d..1d], windSpeed=" + windSpeed
                + " [m/s], windAngle=" + windAngle
                + " [rad]}";
    }
}
