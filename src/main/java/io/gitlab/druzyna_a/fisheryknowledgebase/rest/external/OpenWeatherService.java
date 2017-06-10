package io.gitlab.druzyna_a.fisheryknowledgebase.rest.external;

/**
 *
 * @author Damian Terlecki
 */
import io.gitlab.druzyna_a.fisheryknowledgebase.model.external.OpenWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    int CALL_PER_MIN_LIMIT = 60;
    String OPEN_WEATHER_MAP_APP_ID = "78da521e278e3005eb2538763f5d31df";
    String BASE_URL = "http://api.openweathermap.org";

    @GET("/data/2.5/weather")
    Call<OpenWeather> getWeather(@Query("lon") double lng, @Query("lat") double lat, @Query("APPID") String appId);

    @GET("/data/2.5/forecast")
    Call<OpenWeather.Forecast> getForecast(@Query("lon") double lng, @Query("lat") double lat, @Query("APPID") String appId);
}
