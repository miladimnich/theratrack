package org.dci.theratrack.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.exceptions.InvalidRequestException;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

  @Autowired
  private AppointmentRepository appointmentRepository;

  /**
   * Creates a new appointment.
   *
   * @param appointment the appointment to create
   * @return the created appointment
   */
  public Appointment createAppointment(Appointment appointment) {
    if (appointment == null) {
      throw new InvalidRequestException("Appointment cannot be null.");
    }
    return appointmentRepository.save(appointment);
  }

  /**
   * Retrieves an appointment by its ID.
   *
   * @param appointmentId the ID of the appointment
   * @return the appointment
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public Appointment getAppointment(Long appointmentId) {
    return appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
  }

  public List<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  /**
   * Retrieves appointments for a specific patient.
   *
   * @param patient the patient whose appointments are to be retrieved
   * @return a list of appointments
   * @throws InvalidRequestException if the patient is null or has no ID
   */
  public List<Appointment> getAppointmentsByPatient(Patient patient) {
    if (patient == null || patient.getId() == null) {
      throw new InvalidRequestException("Patient or patient ID cannot be null.");
    }
    return appointmentRepository.getAppointmentsByPatientId(patient.getId());
  }

  /**
   * Updates an existing appointment.
   *
   * @param appointment the appointment to update
   * @return the updated appointment
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public Appointment updateAppointment(Appointment appointment) {
    if (appointment == null || appointment.getId() == null) {
      throw new InvalidRequestException("Appointment or appointment ID cannot be null.");
    }

    // Check if the appointment exists before updating
    if (!appointmentRepository.existsById(appointment.getId())) {
      throw new ResourceNotFoundException("Appointment not found with ID: " + appointment.getId());
    }

    return appointmentRepository.save(appointment);
  }

  /**
   * Deletes an appointment by its ID.
   *
   * @param appointmentId the ID of the appointment to delete
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public void deleteAppointment(Long appointmentId) {
    if (!appointmentRepository.existsById(appointmentId)) {
      throw new ResourceNotFoundException("Appointment not found with ID: " + appointmentId);
    }
    appointmentRepository.deleteById(appointmentId);
  }


  public void updateSessionDetails(Long appointmentId, String additionalNotes) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new RuntimeException("Appointment not found"));

    appointment.setNotes(additionalNotes);

    appointmentRepository.save(appointment);
  }
}
