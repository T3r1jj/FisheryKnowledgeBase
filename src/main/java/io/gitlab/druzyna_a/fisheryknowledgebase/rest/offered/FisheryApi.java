package io.gitlab.druzyna_a.fisheryknowledgebase.rest.offered;

import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Coordinate;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Fishery;
import io.gitlab.druzyna_a.fisheryknowledgebase.model.offered.Weather;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Damian Terlecki
 */
@Api(value = "/fishery", tags = {"fishery"}, description = "Operations about fisheries")
@RequestMapping("/fishery")
public interface FisheryApi {

    @ApiOperation(httpMethod = "GET", value = "Fetch information about fishery hourly forecast by coordinates in next 5 days")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Information about fishery weather forecast", response = Weather.class, responseContainer = "List")
        , @ApiResponse(code = 404, message = "Weather forecast not found for given time", response = Void.class)
        , @ApiResponse(code = 429, message = "Too many requests per minute", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)})
    @RequestMapping(path = "/forecast", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<Weather>> fetchFisheryForecast(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng,
            @ApiParam(value = "Forecast epoch seconds UTC") @RequestParam(required = false, defaultValue = "0") long time,
            @ApiParam(value = "Total forecast hours", defaultValue = "1", allowableValues = "range[1, 24]") @RequestParam(required = false, defaultValue = "1") int range);

    @ApiOperation(httpMethod = "GET", value = "Fetch information about fishery current weather by coordinates")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Information about current fishery weather", response = Weather.class)
        , @ApiResponse(code = 429, message = "Too many requests per minute", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)})
    @RequestMapping(path = "/weather", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Weather> fetchFisheryWeather(
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng);

    @ApiOperation(httpMethod = "GET", value = "Fetch fisheries by country")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of fisheries in given country", response = Fishery.class, responseContainer = "List")
        , @ApiResponse(code = 404, message = "Country not found", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<Fishery>> fetchFisheries(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode);

}
