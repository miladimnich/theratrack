package org.dci.theratrack.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.enums.TreatmentStatus;
import org.dci.theratrack.service.EnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

  @Autowired
  private final EnumService enumService;

  public EnumController(EnumService enumService) {
    this.enumService = enumService;
  }

  @GetMapping("/difficulty-levels")
  public ResponseEntity<List<String>> getDifficultyLevels() {
    List<String> difficultyLevels = enumService.getDifficultyLevels();
    return ResponseEntity.ok(difficultyLevels);
  }

  @GetMapping("/treatment-statuses")
  public ResponseEntity<List<String>> getTreatmentStatuses() {
    List<String> treatmentStatuses = enumService.getTreatmentStatuses();
    return ResponseEntity.ok(treatmentStatuses);
  }
}
