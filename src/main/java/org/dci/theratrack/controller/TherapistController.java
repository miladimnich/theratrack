package org.dci.theratrack.controller;

import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
public class TherapistController {

    @Autowired
    private TherapistService therapistService;

    @PostMapping
    public ResponseEntity<Therapist> createTherapist(@RequestBody Therapist therapist) {
        return ResponseEntity.ok(therapistService.createTherapist(therapist));
    }

    @GetMapping
    public List<Therapist> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Therapist> getTherapistById(@PathVariable Long id) {
        return therapistService.getTherapistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Therapist> updateTherapist(@PathVariable Long id, @RequestBody Therapist therapist) {
        return ResponseEntity.ok(therapistService.updateTherapist(id, therapist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTherapist(@PathVariable Long id) {
        therapistService.deleteTherapist(id);
        return ResponseEntity.noContent().build();
    }
}