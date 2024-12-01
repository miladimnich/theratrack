package org.dci.theratrack.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.service.TreatmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentsController {

  private final TreatmentService treatmentService;

  public TreatmentsController(TreatmentService treatmentService) {
    this.treatmentService = treatmentService;
  }


  @GetMapping
  public ResponseEntity<List<Treatment>> getAllTreatments() {
    List<Treatment> treatments = treatmentService.getAllTreatments();
    return ResponseEntity.ok(treatments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Treatment> getTreatment(@PathVariable Long id) {
    Treatment treatment = treatmentService.getTreatment(id);
    return ResponseEntity.ok(treatment);
  }


  @PostMapping
  public ResponseEntity<Treatment> addTreatment(@RequestBody Treatment treatmentRequest) {
    Treatment createdTreatment = treatmentService.addTreatment(treatmentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTreatment);

  }



  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
    treatmentService.deleteTreatment(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Treatment> updateTreatment(
      @PathVariable Long id,
      @RequestBody @Valid Treatment treatmentRequest
  ) {
    // Ensure the treatment ID matches
    treatmentRequest.setId(id);
    Treatment updatedTreatment = treatmentService.updateTreatment(treatmentRequest);
    return ResponseEntity.ok(updatedTreatment);
  }

}
