package org.dci.theratrack.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.enums.AppointmentStatus;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.request.AppointmentDTO;
import org.dci.theratrack.request.TherapySessionHistoryDTO;
import org.dci.theratrack.request.TreatmentDTO;
import org.dci.theratrack.service.AppointmentService;
import org.dci.theratrack.service.PatientService;
import org.dci.theratrack.service.TreatmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  PatientService patientService;

  @PostMapping
  public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentRequest) {
    Long patientId = appointmentRequest.getPatientId();
    Patient patient = patientService.getPatientById(patientId);
    if (patient == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Patient not found with ID: " + patientId);
    }

    Appointment appointment = modelMapper.map(appointmentRequest, Appointment.class);
    appointment.setPatient(patient);

    Appointment createdAppointment = appointmentService.createAppointment(appointment);

    // Convert the created appointment to a DTO and return the response
    AppointmentDTO appointmentDTO = modelMapper.map(createdAppointment, AppointmentDTO.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(appointmentDTO);
  }

  @PutMapping("/{appointmentId}/treatments")
  public ResponseEntity<?> addTreatmentDetail(@PathVariable Long appointmentId,
      @RequestBody TreatmentDTO treatmentDTO) {
    Appointment appointment = appointmentService.getAppointment(appointmentId);

    if (appointment == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Appointment not found with ID: " + appointmentId);
    }

    DifficultyLevel difficultyLevel = treatmentDTO.getDifficultyLevel();
    if (difficultyLevel == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Difficulty level is missing in the treatment details.");
    }

    // Map TreatmentDTO to Treatment entity
    Treatment treatment = modelMapper.map(treatmentDTO, Treatment.class);
    treatment.setDifficultyLevel(difficultyLevel);
    treatment.setAppointment(appointment);

    treatment.setNotes(treatmentDTO.getNotes());

    appointment.getTreatments().add(treatment);

    appointmentService.updateAppointment(appointment);

    return ResponseEntity.status(HttpStatus.OK)
        .body("Treatment details added successfully.");
  }


  @GetMapping
  public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
    List<Appointment> appointments = appointmentService.getAllAppointments();
    List<AppointmentDTO> appointmentDTOs = appointments.stream()
        .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
        .collect(Collectors.toList());
    return ResponseEntity.ok(appointmentDTOs);
  }


  @PreAuthorize("hasRole('ROLE_PATIENT')")
  @GetMapping("/history/{patientId}")
  public ResponseEntity<List<TherapySessionHistoryDTO>> getTherapySessionHistory(
      @PathVariable Long patientId) {
    List<TherapySessionHistoryDTO> history = appointmentService.getTherapySessionHistory(patientId);
    if (history.isEmpty()) {
      return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
    return ResponseEntity.ok(history);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_THERAPIST')")
  @PutMapping("/{appointment_id}/notes")
  public ResponseEntity<String> updateSessionDetails(
      @PathVariable ("appointment_id") Long id,
      @RequestBody AppointmentDTO appointmentDTO) {
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