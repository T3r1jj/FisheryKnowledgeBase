package io.gitlab.druzyna_a.fisheryknowledgebase.model.external;

/**
 *
 * @author Damian Terlecki
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Weather;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class OpenWeather extends Weather {

    private List<Condition> conditions = new LinkedList<>();
    private String cityName;
    private long sunrise;
    private long sunset;
    private String country;
    private double cloudiness;
    private String rainPast3h;
    private String snowPast3h;

    OpenWeather(double p, double T, double phi, double windSpeed, double windAngle, long epochSec) {
        super(p, T, phi, windSpeed, windAngle, epochSec);
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCloudiness(double cloudiness) {
        this.cloudiness = cloudiness;
    }

    public void setRainPast3h(String rainPast3h) {
        this.rainPast3h = rainPast3h;
    }

    public void setSnowPast3h(String snowPast3h) {
        this.snowPast3h = snowPast3h;
    }

    void addCondition(Condition condition) {
        conditions.add(condition);
    }

    @Override
    public String toString() {
        return super.toString() + "\nOpenWeather{"
                + "conditions='" + conditions + '\''
                + ", cityName='" + cityName + '\''
                + ", time='" + time + '\''
                + ", sunrise='" + sunrise + '\''
                + ", sunset='" + sunset + '\''
                + ", country='" + country + '\''
                + ", cloudiness='" + cloudiness + '\''
                + ", rainPast3h='" + rainPast3h + '\''
                + ", snowPast3h='" + snowPast3h + '\''
                + '}';
    }

    public static class Condition {

        String main;

        String description;

        public Condition(String main, String description) {
            this.main = main;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Condition{"
                    + "main='" + main + '\''
                    + ", description='" + description + '\''
                    + '}';
        }

    }

    public static class OpenWeatherDeserializer implements JsonDeserializer<OpenWeather> {

        @Override
        public OpenWeather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonRoot = json.getAsJsonObject();
            JsonObject main = jsonRoot.get("main").getAsJsonObject();
            double p = main.get("pressure").getAsDouble() * 100d;
            double T = main.get("temp").getAsDouble();
            double phi = main.get("humidity").getAsDouble() / 100d;
            JsonObject wind = jsonRoot.get("wind").getAsJsonObject();
            double windSpeed = ktToMps(wind.get("speed").getAsDouble());
            double windAngle = Math.toRadians(wind.get("deg").getAsDouble());
            long time = jsonRoot.get("dt").getAsLong();
            OpenWeather openWeather = new OpenWeather(p, T, phi, windSpeed, windAngle, time);
            if (jsonRoot.has("name")) {
                openWeather.setCityName(jsonRoot.get("name").getAsString());
            }
            JsonArray weather = jsonRoot.get("weather").getAsJsonArray();
            for (JsonElement jsonElement : weather) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                openWeather.addCondition(new Condition(
                        jsonObject.get("main").getAsString(),
                        jsonObject.get("description").getAsString())
                );
            }
            JsonObject clouds = jsonRoot.get("clouds").getAsJsonObject();
            openWeather.setCloudiness(clouds.get("all").getAsDouble() / 100d);
            JsonObject sys = jsonRoot.get("sys").getAsJsonObject();
            if (sys.has("sunrise")) {
                openWeather.setSunrise(sys.get("sunrise").getAsLong());
            }
            if (sys.has("sunset")) {
                openWeather.setSunset(sys.get("sunset").getAsLong());
            }
            if (sys.has("country")) {
                openWeather.setCountry(sys.get("country").getAsString());
            }
            return openWeather;
        }

    }

    public static class Forecast {

        @SerializedName("list")
        public OpenWeather[] data;
    }

}
