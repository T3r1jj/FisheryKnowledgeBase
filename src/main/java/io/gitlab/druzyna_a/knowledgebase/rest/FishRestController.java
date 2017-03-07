package io.gitlab.druzyna_a.knowledgebase.rest;

import io.gitlab.druzyna_a.knowledgebase.model.Equipment;
import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.Protection;
import io.swagger.annotations.ApiParam;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Damian Terlecki
 */
@RestController
public class FishRestController implements FishApi {

    @Override
    public ResponseEntity<Fish> fetchFish(@ApiParam(value = "Name of the fish", required = true) @PathVariable("name") String name) {
        return ResponseEntity.ok().body(new Fish());
    }

    @Override
    public ResponseEntity<List<Protection>> fetchFishProtections(@ApiParam(value = "Name of the fish", allowableValues = "string", required = true)
            @RequestParam(required = true) String name) {
        return ResponseEntity.ok().body(Arrays.asList(new Protection[]{new Protection(), new Protection(), new Protection()}));
    }

    @Override
    public ResponseEntity<List<String>> fetchFishImages(@ApiParam(value = "Name of the fish", required = true)
            @RequestParam String name) {
        return ResponseEntity.ok().body(Arrays.asList(new String[]{"https://cdn.pixabay.com/photo/2015/12/06/20/05/fishing-1079915_960_720.jpg"}));
    }

    @Override
    public ResponseEntity<Boolean> exists(@ApiParam(value = "Name of the fish", required = true) @RequestParam String name,
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng) {
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @Override
    public ResponseEntity<List<String>> fetchFishes() {
        return ResponseEntity.ok(Arrays.asList(new String[]{"perch", "roach"}));
    }

    @Override
    public ResponseEntity<Equipment> fetchBestEquipment(@ApiParam(value = "Name of the fish", required = true) @RequestParam("name") String name) {
        return ResponseEntity.ok().body(new Equipment());
    }

}
