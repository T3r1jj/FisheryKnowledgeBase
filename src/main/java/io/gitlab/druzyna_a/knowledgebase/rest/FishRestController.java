package io.gitlab.druzyna_a.knowledgebase.rest;

import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.Protection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Damian Terlecki
 */
@Api(value = "/fish", tags = {"fish"}, description = "Operations about fish")
@RestController
@RequestMapping("/fish")
public class FishRestController {

    @ApiOperation(httpMethod = "GET", value = "Fetch additional information about fish by name")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Information about fish", response = Fish.class)
        ,
        @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Fish> fetchFishInformation(@ApiParam(value = "Name of the fish", allowableValues = "string", required = true) @RequestParam("name") String name) {
        return ResponseEntity.ok().body(new Fish());
    }

    @ApiOperation(httpMethod = "GET", value = "Fetch additional protection information about fish by name")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Information about fish protection", response = Protection.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "No information about protection protection (fish not protected AFAIK)", response = Void.class)
        ,
        @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @RequestMapping(path = "/protection", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Protection>> fetchFishProtectionInformation(@ApiParam(value = "Name of the fish", allowableValues = "string", required = true) @RequestParam("name") String name) {
        return ResponseEntity.ok().body(Arrays.asList(new Protection[]{new Protection(), new Protection(), new Protection()}));
    }

}
