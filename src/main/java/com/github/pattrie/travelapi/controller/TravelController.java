package com.github.pattrie.travelapi.controller;

import com.github.pattrie.travelapi.model.Travel;
import com.github.pattrie.travelapi.service.TravelService;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api-travel/v1/travels")
public class TravelController {
    private TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public ResponseEntity<List<Travel>> find() {
        if (travelService.find().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        log.info(travelService.find());
        return ResponseEntity.ok(travelService.find());
    }

    @PostMapping
    public ResponseEntity<Travel> create(@RequestBody final JSONObject travel) {
        try {
            if (!travelService.isJsonValid(travel.toString()))
                return ResponseEntity.badRequest().body(null);

            Travel travelCreated = travelService.create(travel);
            var uri =
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path(travelCreated.getOrderNumber())
                            .build()
                            .toUri();

            if (!travelService.isStartDateGreaterThanEndDate(travelCreated)) {
                travelService.add(travelCreated);
                return ResponseEntity.created(uri).body(null);
            }

            log.error("The start date is greater than end date.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);

        } catch (Exception e) {
            log.error("JSON fields are not parsable. " + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @PutMapping(
            path = "/{id}",
            produces = {"application/json"})
    public ResponseEntity<Travel> update(
            @PathVariable("id") Integer id, @RequestBody JSONObject travel) {
        try {
            if (!travelService.isJsonValid(travel.toString()))
                return ResponseEntity.badRequest().body(null);
            Travel travelToUpdate = travelService.findById(id);
            if (travelToUpdate == null) {
                log.error("Travel not found.");
                return ResponseEntity.notFound().build();
            }
            travelToUpdate = travelService.update(travelToUpdate, travel);
            return ResponseEntity.ok(travelToUpdate);
        } catch (Exception e) {
            log.error("JSON fields are not parsable." + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete() {
        try {
            travelService.delete();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}