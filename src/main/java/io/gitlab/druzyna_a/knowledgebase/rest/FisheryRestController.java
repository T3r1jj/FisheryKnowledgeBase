package io.gitlab.druzyna_a.knowledgebase.rest;

import io.gitlab.druzyna_a.knowledgebase.model.Fishery;
import io.gitlab.druzyna_a.knowledgebase.model.Weather;
import io.swagger.annotations.ApiParam;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class FisheryRestController implements FisheryApi {

    @Override
    public ResponseEntity<List<Fishery>> fetchFisheries(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode) {
        return ResponseEntity.ok().body(Arrays.asList(new Fishery[]{new Fishery(), new Fishery()}));
    }

    @Override
    public ResponseEntity<Weather> fetchFisheryWeather(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng) {
        return ResponseEntity.ok().body(new Weather());
    }

    @Override
    public ResponseEntity<List<Weather>> fetchFisheryForecast(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng,
            @ApiParam(value = "Forecast epoch seconds UTC") @RequestParam(required = false, defaultValue = "0") long time,
            @ApiParam(value = "Total forecast hours", defaultValue = "1", allowableValues = "range[1, 24]") @RequestParam(required = false, defaultValue = "1") int range) {
        List<Weather> forecast = new LinkedList<>();
        for (int i = 0; i < range; i++) {
            forecast.add(new Weather());
        }
        return ResponseEntity.ok().body(forecast);
    }

}
