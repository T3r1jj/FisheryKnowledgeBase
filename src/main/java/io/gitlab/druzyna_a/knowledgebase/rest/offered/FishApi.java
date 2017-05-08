package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.offered.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishImage;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishName;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishProtection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Damian Terlecki
 */
@Api(value = "/fish", tags = {"fish"}, description = "Operations about fishes")
@RequestMapping("/fish")
public interface FishApi {

    @ApiOperation(httpMethod = "GET", value = "Fetch additional information about fish by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Information about fish", response = Fish.class)
        , @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)})
    @RequestMapping(path = "/{name}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Fish> fetchFish(@ApiParam(value = "Name of the fish", required = true) @PathVariable("name") String name);

    @ApiOperation(httpMethod = "GET", value = "Fetch all fish names")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Fish names", response = FishName.class, responseContainer = "List")
        , @ApiResponse(code = 404, message = "Wrong country code", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)
    })
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<FishName>> fetchFishes(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode);

    @ApiOperation(httpMethod = "GET", value = "Fetch additional protection information about fish by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Information about fish protection", response = FishProtection.class)
        , @ApiResponse(code = 404, message = "Fish or protection not found", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)})
    @RequestMapping(path = "/protection", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<FishProtection> fetchFishProtection(@ApiParam(value = "Scientific name of the fish", required = true)
            @RequestParam String name);

    @ApiOperation(httpMethod = "GET", value = "Fetch additional images of fish by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Images of fish", response = String.class, responseContainer = "List")
        , @ApiResponse(code = 404, message = "No images about fish with such name", response = Void.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)
    })
    @RequestMapping(path = "images", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<FishImage>> fetchFishImages(@ApiParam(value = "Scientific name of the fish", required = true)
            @RequestParam String name);

    @ApiOperation(httpMethod = "GET", value = "Check if the fish can live at given coordinates")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Fish can live at give coordinates", response = Boolean.class)
        , @ApiResponse(code = 502, message = "Error while connecting to upstream server, try again later", response = Void.class)
    })
    @RequestMapping(path = "exists", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Boolean> exists(@ApiParam(value = "Name of the fish", required = true) @RequestParam String name,
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng,
            @ApiParam(value = "Fishery radius in meters", required = true) @RequestParam int radius);

}
