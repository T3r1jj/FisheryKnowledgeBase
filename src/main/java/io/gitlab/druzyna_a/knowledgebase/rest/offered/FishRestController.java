package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.FishImage;
import io.gitlab.druzyna_a.knowledgebase.model.FishName;
import io.gitlab.druzyna_a.knowledgebase.model.FishProtection;
import io.gitlab.druzyna_a.knowledgebase.model.external.BoundingBox;
import io.gitlab.druzyna_a.knowledgebase.scraping.FishScraper;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FishScraper fishScraper;

    @Override
    public ResponseEntity<Fish> fetchFish(@ApiParam(value = "Name of the fish", required = true) @PathVariable("name") String name) {
        try {
            final Optional<Fish> fish = fishScraper.scrapeFish(name);
            if (fish.isPresent()) {
                return ResponseEntity.ok(fish.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (IOException ex) {
            Logger.getLogger(FishRestController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(502).build();
        }
    }

    @Override
    public ResponseEntity<FishProtection> fetchFishProtections(@ApiParam(value = "Name of the fish", allowableValues = "string", required = true)
            @RequestParam(required = true) String name) {
        try {
            final Optional<FishProtection> fishProtection = fishScraper.scrapeFishProtection(name);
            if (fishProtection.isPresent()) {
                return ResponseEntity.ok(fishProtection.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (IOException ex) {
            Logger.getLogger(FishRestController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(502).build();
        }
    }

    @Override
    public ResponseEntity<List<FishImage>> fetchFishImages(@ApiParam(value = "Name of the fish", required = true)
            @RequestParam String name) {
        try {
            return ResponseEntity.ok(fishScraper.scrapeImages(name));
        } catch (IOException ex) {
            Logger.getLogger(FishRestController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(502).build();
        }
    }

    @Override
    public ResponseEntity<Boolean> exists(@ApiParam(value = "Name of the fish", required = true) @RequestParam String name,
            @ApiParam(value = "Fishery latitude coordinate", required = true) @RequestParam double lat,
            @ApiParam(value = "Fishery longtitude coordinate", required = true) @RequestParam double lng,
            @ApiParam(value = "Fishery radius in meters", required = true) @RequestParam int radius) {
        try {
            return ResponseEntity.ok().body(fishScraper.scrapeExists(BoundingBox.getBBoxCoordinates(lat, lng, radius), name));
        } catch (IOException ex) {
            Logger.getLogger(FishRestController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(502).build();
        }
    }

    @Override
    public ResponseEntity<List<FishName>> fetchFishes(@ApiParam(value = "Country code ISO 3166-1 alpha-2", required = true, example = "PL") @RequestParam String countryCode) {
        try {
            final Optional<List<FishName>> fishNames = fishScraper.scrapeFishNames(countryCode);
            if (fishNames.isPresent()) {
                return ResponseEntity.ok(fishNames.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (IOException ex) {
            Logger.getLogger(FishRestController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(502).build();
        }
    }

}
