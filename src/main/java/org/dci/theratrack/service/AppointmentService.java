package org.dci.theratrack.service;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.annotation.After;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Diagnosis;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.enums.AppointmentStatus;
import org.dci.theratrack.exceptions.InvalidRequestException;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.repository.AppointmentRepository;
import org.dci.theratrack.repository.DiagnosisRepository;
import org.dci.theratrack.repository.PatientRepository;
import org.dci.theratrack.repository.TherapistRepository;
import org.dci.theratrack.repository.TreatmentRepository;
import org.dci.theratrack.request.AppointmentRequest;
import org.dci.theratrack.request.TreatmentRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AppointmentService {

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  TherapistRepository therapistRepository;

  @Autowired
  PatientRepository patientRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  TreatmentRepository treatmentRepository;

  /**
   * Creates a new appointment.
   *
   * @param request the appointment to create
   * @return the created appointment
   */

  public Appointment createAppointment(AppointmentRequest request) {
    Appointment appointment = request.getAppointment();
    if (appointment == null) {
      throw new InvalidRequestException("Appointment details cannot be null.");
    }

    // Ensure Patient and Therapist are assigned before saving the Appointment
    Patient patient = patientRepository.findById(request.getPatient().getId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Patient not found with ID: " + request.getPatient().getId()));
    appointment.setPatient(patient);

    Therapist therapist = therapistRepository.findById(request.getTherapist().getId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Therapist not found with ID: " + request.getTherapist().getId()));
    appointment.setTherapist(therapist);

    // Save the appointment to generate an ID
    appointment = appointmentRepository.save(appointment);


    // Handle treatment assignment or creation
    if (request.getTreatment() != null) {
      Treatment treatment;
      if (request.getTreatment().getId() != null) {
        // If treatment ID is provided, fetch the existing treatment
        treatment = treatmentRepository.findById(request.getTreatment().getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Treatment not found with ID: " + request.getTreatment().getId()));
      } else {
        // If no treatment ID, create a new treatment
        treatment = new Treatment();
      }

      // Map the fields from TreatmentRequest to Treatment
      treatment.setName(request.getTreatment().getName());
      treatment.setDescription(request.getTreatment().getDescription());
      treatment.setDuration(request.getTreatment().getDuration());
      treatment.setDifficultyLevel(request.getTreatment().getDifficultyLevel());
      treatment.setTreatmentStatus(request.getTreatment().getTreatmentStatus());
      treatment.setNotes(request.getTreatment().getNotes());
      treatment.setAppointment(appointment);

      // Save the treatment and link it to the appointment
      treatment = treatmentRepository.save(treatment);

      // Add treatment to the appointment
      if (appointment.getTreatments() == null) {
        appointment.setTreatments(new ArrayList<>());
      }
      appointment.getTreatments().add(treatment);
    }

    // Save the final appointment state with treatments linked
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
    if (appointmentId == null) {
      throw new InvalidRequestException("Appointment id cannot be null.");
    }
    return appointmentRepository.findById(appointmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
  }


  /**
   * Retrieves appointments for a specific patient.
   *
   * @param patientId the patient whose appointments are to be retrieved
   * @return a list of appointments
   * @throws InvalidRequestException if the patient is null or has no ID
   */
  public List<Appointment> getAppointmentsByPatientId(Long patientId) {
    return appointmentRepository.getAppointmentsByPatientId(patientId);
  }

  public List<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  /**
   * Updates an existing appointment.
   *
   * @param updatedAppointment the appointment to update
   * @return the updated appointment
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment) {
    if (updatedAppointment == null) {
      throw new InvalidRequestException("Updated appointment cannot be null.");
    }

    // Retrieve the existing appointment
    Appointment existingAppointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

    modelMapper.map(updatedAppointment, existingAppointment);

    // Handle associations explicitly if needed
    if (updatedAppointment.getPatient() != null
        && updatedAppointment.getPatient().getId() != null) {
      Patient patient = patientRepository.findById(updatedAppointment.getPatient().getId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Patient not found with ID: " + updatedAppointment.getPatient().getId()));
      existingAppointment.setPatient(patient);
    }

    if (updatedAppointment.getTherapist() != null
        && updatedAppointment.getTherapist().getId() != null) {
      Therapist therapist = therapistRepository.findById(updatedAppointment.getTherapist().getId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Therapist not found with ID: " + updatedAppointment.getTherapist().getId()));
      existingAppointment.setTherapist(therapist);
    }


    // Handle the treatment update if it's provided
    if (updatedAppointment.getTreatments() != null && !updatedAppointment.getTreatments().isEmpty()) {
      // Clear the existing treatments and add the new ones
      existingAppointment.getTreatments().clear(); // Remove existing treatments

      // Add the treatments from the updated appointment to the existing one
      for (Treatment treatment : updatedAppointment.getTreatments()) {
        // Check if the treatment exists in the database, if so, associate it
        if (treatment.getId() != null) {
          Treatment existingTreatment = treatmentRepository.findById(treatment.getId())
              .orElseThrow(() -> new ResourceNotFoundException(
                  "Treatment not found with ID: " + treatment.getId()));
          // Set the existing treatment to the current appointment
          existingTreatment.setAppointment(existingAppointment);  // Ensure treatment's appointment is updated

          existingAppointment.getTreatments()
              .add(existingTreatment); // Add the existing treatment to the appointment
        } else {
          // If the treatment doesn't exist in the database (new treatment), add it as is
          treatment.setAppointment(existingAppointment);  // Ensure the appointment reference is set
          // If the treatment is new (i.e., does not have an ID), we might want to create it
          existingAppointment.getTreatments().add(treatment);
        }
      }
    }
    // Save and return the updated appointment
    return appointmentRepository.save(existingAppointment);
  }


  /**
   * Deletes an appointment by its ID.
   *
   * @param appointmentId the ID of the appointment to delete
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public void deleteAppointment(Long appointmentId) {
    if (appointmentId == null) {
      throw new InvalidRequestException("Appointment ID cannot be null.");
    }

    if (!appointmentRepository.existsById(appointmentId)) {
      throw new ResourceNotFoundException("Appointment not found with ID: " + appointmentId);
    }
    appointmentRepository.deleteById(appointmentId);
  }


}