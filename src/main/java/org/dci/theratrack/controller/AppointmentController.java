package org.dci.theratrack.controller;

import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.request.AppointmentRequest;
import org.dci.theratrack.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;


  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THERAPIST')")
  @PutMapping("/{id}/notes")
  public ResponseEntity<String> updateSessionDetails(
      @PathVariable Long id,
      @RequestBody AppointmentRequest appointmentDTO) {
    if (appointmentDTO.getNotes() == null || appointmentDTO.getNotes().length() > 2000) {
      return ResponseEntity.badRequest().body("Notes must be non-null and under 2000 characters.");
    }

    try {
      appointmentService.updateSessionDetails(id, appointmentDTO.getNotes());

      return ResponseEntity.ok("Session details updated successfully.");
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Appointment not found: " + ex.getMessage());
    }

  }
}
