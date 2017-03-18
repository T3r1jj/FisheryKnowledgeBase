package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import com.google.gson.GsonBuilder;
import io.gitlab.druzyna_a.knowledgebase.model.offered.Fishery;
import io.gitlab.druzyna_a.knowledgebase.model.utils.IsoUtil;
import io.gitlab.druzyna_a.knowledgebase.model.offered.Weather;
import io.gitlab.druzyna_a.knowledgebase.model.external.OpenWeather;
import io.gitlab.druzyna_a.knowledgebase.model.external.OverpassFisheryData;
import io.gitlab.druzyna_a.knowledgebase.rest.external.OpenWeatherService;
import static io.gitlab.druzyna_a.knowledgebase.rest.external.OpenWeatherService.OPEN_WEATHER_MAP_APP_ID;
import io.gitlab.druzyna_a.knowledgebase.rest.external.OverpassApi;
import io.gitlab.druzyna_a.knowledgebase.rest.external.WeatherCallCounter;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class FisheryRestController implements FisheryApi {

    @Autowired
    private WeatherCallCounter weatherCallCounter;

    @Override
    public ResponseEntity<List<Fishery>> fetchFisheries(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode) {
        if (!IsoUtil.isValidCountry(countryCode)) {
            return ResponseEntity.notFound().build();
        }
        String query = "[out:json];(area[\"ISO3166-1\"=\"" + countryCode + "\"][admin_level=2];)->.a;node[leisure=fishing](area.a);out;";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OverpassApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverpassApi api = retrofit.create(OverpassApi.class);
        Call<OverpassFisheryData> overpassCall = api.getLocations(query);
        try {
            Response<OverpassFisheryData> response = overpassCall.execute();
            List<Fishery> fisheries = response.body().toFisheries();
            return ResponseEntity.ok().body(fisheries);
        } catch (IOException ex) {
            Logger.getLogger(FisheryRestController.class.getName()).log(Level.SEVERE, null, ex);
            return generateUnexpectedResponse(ex);
        }
    }

    @Override
    public ResponseEntity<Weather> fetchFisheryWeather(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng) {
        if (!weatherCallCounter.canCall()) {
            return ResponseEntity.status(429).build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherService.BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().registerTypeAdapter(OpenWeather.class, new OpenWeather.OpenWeatherDeserializer()
                                ).create()
                        )
                ).build();
        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        Call<OpenWeather> openWeatherCall = service.getWeather(lng, lat, OPEN_WEATHER_MAP_APP_ID);
        weatherCallCounter.call();
        try {
            Response<OpenWeather> response = openWeatherCall.execute();
            OpenWeather weather = response.body();
            return ResponseEntity.ok().body(weather);
        } catch (IOException ex) {
            return generateUnexpectedResponse(ex);
        }
    }

    @Override
    public ResponseEntity<List<Weather>> fetchFisheryForecast(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng,
            @ApiParam(value = "Forecast epoch seconds UTC, default = from now") @RequestParam(required = false, defaultValue = "0") long time,
            @ApiParam(value = "Total forecast hours", defaultValue = "3", allowableValues = "range[3, 24]") @RequestParam(required = false, defaultValue = "1") int range) {
        if (!weatherCallCounter.canCall()) {
            return ResponseEntity.status(429).build();
        }
        if (time == 0) {
            time = Instant.now().getEpochSecond();
        }
        long requestedTime = time;
        long epochSecondsHourAgo = Instant.now().getEpochSecond() - (60 * 60);
        long epochSecondsIn5days = Instant.now().getEpochSecond() + 5 * 60 * 60 * 24;
        if (time < epochSecondsHourAgo || time > epochSecondsIn5days) {
            return ResponseEntity.status(404).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherService.BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().registerTypeAdapter(OpenWeather.class, new OpenWeather.OpenWeatherDeserializer()
                                ).create()
                        )
                ).build();
        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        Call<OpenWeather.Forecast> openWeatherCall = service.getForecast(lng, lat, OPEN_WEATHER_MAP_APP_ID);
        weatherCallCounter.call();
        try {
            Response<OpenWeather.Forecast> response = openWeatherCall.execute();
            List<Weather> forecast = Arrays.asList(response.body().data);
            forecast = forecast.stream().filter(weather -> ((weather.time >= requestedTime) && (weather.time <= (requestedTime + range * 60 * 60)))).collect(Collectors.toList());
            return ResponseEntity.ok().body(forecast);
        } catch (IOException ex) {
            return generateUnexpectedResponse(ex);
        }
    }

    private ResponseEntity generateUnexpectedResponse(IOException ex) {
        Logger.getLogger(FisheryRestController.class.getName()).log(Level.SEVERE, null, ex);
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exception = sw.toString();
        return ResponseEntity.status(502).header("Warning", exception).build();
    }

}
