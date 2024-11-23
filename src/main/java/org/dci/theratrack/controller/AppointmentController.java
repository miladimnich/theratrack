package org.dci.theratrack.controller;

import java.util.List;
import java.util.Map;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.request.AppointmentRequest;
import org.dci.theratrack.service.AppointmentService;
import org.dci.theratrack.service.PatientService;
import org.dci.theratrack.service.TreatmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/appointments")
public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  PatientService patientService;

  @Autowired
  private TreatmentService treatmentService;

  @PostMapping
  public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
    System.out.println("Received request: " + request);
    return ResponseEntity.ok(appointmentService.createAppointment(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
    Appointment appointment = appointmentService.getAppointment(id);
    return ResponseEntity.ok(appointment);
  }

  @GetMapping("/patient/{patientId}")
  public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(
      @PathVariable Long patientId) {
    List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
    if (appointments.isEmpty()) {
      throw new ResourceNotFoundException(
          "No appointments found for patient with ID: " + patientId);
    }
    return ResponseEntity.ok(appointments);
  }

  @GetMapping
  public List<Appointment> getAllAppointments() {
    return appointmentService.getAllAppointments();
  }

  @PutMapping("/{appointmentId}")
  public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId,
      @RequestBody Appointment updatedAppointment) {
    return ResponseEntity.ok(
        appointmentService.updateAppointment(appointmentId, updatedAppointment));

  }

  @DeleteMapping("/{appointmentId}")
  public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointmentId) {
    appointmentService.deleteAppointment(appointmentId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{appointmentId}/treatments")
  public ResponseEntity<Treatment> addTreatmentDetail(
      @PathVariable Long appointmentId,
      @RequestBody Treatment treatmentRequest) {

    Treatment savedTreatment = appointmentService.addTreatmentToAppointment(appointmentId,
        treatmentRequest);

    return ResponseEntity.ok(savedTreatment);
  }


  @PutMapping("/{appointment_id}/notes")
  public ResponseEntity<Appointment> updateAppointmentNotes(
      @PathVariable("appointment_id") Long appointmentId,
      @RequestBody Map<String, String> requestBody) {

    String updatedNotes = requestBody.get("notes");
    if (updatedNotes == null || updatedNotes.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }

    Appointment updatedAppointment = appointmentService.updateAppointmentNotes(appointmentId,
        updatedNotes);
    return ResponseEntity.ok(updatedAppointment);
  }

}