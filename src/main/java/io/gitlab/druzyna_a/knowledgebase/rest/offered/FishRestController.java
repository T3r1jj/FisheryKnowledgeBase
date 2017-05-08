package io.gitlab.druzyna_a.knowledgebase.rest.offered;

import io.gitlab.druzyna_a.knowledgebase.model.offered.Fish;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishImage;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishName;
import io.gitlab.druzyna_a.knowledgebase.model.offered.FishProtection;
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
    public ResponseEntity<Fish> fetchFish(@ApiParam(value = "Name of the fish", required = true) @PathVariable("name") String name,
            @ApiParam(value = "Force search by common name") @RequestParam(name = "common", required = false, defaultValue = "false") boolean common) {
        try {
            Optional<Fish> fish;
            if (common || name.split(" ").length != 2) {
                fish = fishScraper.scrapeCommonFish(name);
            } else {
                fish = fishScraper.scrapeScientificFish(name);
            }
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
    public ResponseEntity<FishProtection> fetchFishProtection(@ApiParam(value = "Scientific name of the fish", required = true)
            @RequestParam String name) {
        try {
            Optional<FishProtection> fishProtection = fishScraper.scrapeFishProtection(name);
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
    public ResponseEntity<List<FishImage>> fetchFishImages(@ApiParam(value = "Scientific name of the fish", required = true)
            @RequestParam String name) {
        try {
            List<FishImage> images = fishScraper.scrapeImages(name);
            if (images.isEmpty()) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(images);
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
            Optional<List<FishName>> fishNames = fishScraper.scrapeFishNames(countryCode);
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
