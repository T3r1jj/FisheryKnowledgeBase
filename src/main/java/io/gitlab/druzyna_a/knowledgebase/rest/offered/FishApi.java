package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.Equipment;
import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.Protection;
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
        , @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @RequestMapping(path = "/{name}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Fish> fetchFish(@ApiParam(value = "Name of the fish", required = true) @PathVariable("name") String name);

    @ApiOperation(httpMethod = "GET", value = "Fetch additional information about best equipment configuration for catching a given fish")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Best equipment configuration for catching a fish", response = Equipment.class)
        , @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @RequestMapping(path = "/equipment", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Equipment> fetchBestEquipment(@ApiParam(value = "Name of the fish", required = true) @RequestParam("name") String name);

    @ApiOperation(httpMethod = "GET", value = "Fetch all fish names")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Fish names", response = String.class, responseContainer = "List")})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchFishes(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode);

    @ApiOperation(httpMethod = "GET", value = "Fetch additional protection information about fish by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Information about fish protection", response = Protection.class, responseContainer = "List")
        , @ApiResponse(code = 204, message = "No information about protection protection (fish not protected AFAIK)", response = Void.class)
        , @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @RequestMapping(path = "/protection", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<Protection>> fetchFishProtections(@ApiParam(value = "Name of the fish", required = true)
            @RequestParam String name);

    @ApiOperation(httpMethod = "GET", value = "Fetch additional images of fish by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Images of fish", response = String.class, responseContainer = "List")
        , @ApiResponse(code = 404, message = "No images about fish with such name", response = Void.class)})
    @RequestMapping(path = "images", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<List<String>> fetchFishImages(@ApiParam(value = "Name of the fish", required = true)
            @RequestParam String name);

    @ApiOperation(httpMethod = "GET", value = "Check if the fish can live at given coordinates")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Fish can live at give coordinates", response = Boolean.class)
        ,
        @ApiResponse(code = 204, message = "Unknown if fish can live there", response = Void.class)
        , @ApiResponse(code = 404, message = "Not found fish with such name", response = Void.class)})
    @RequestMapping(path = "exists", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<Boolean> exists(@ApiParam(value = "Name of the fish", required = true) @RequestParam String name,
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng);

}
