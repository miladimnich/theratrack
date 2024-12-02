package org.dci.theratrack.service;

import java.util.List;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Diagnosis;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.enums.TreatmentStatus;
import org.dci.theratrack.exceptions.InvalidRequestException;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.repository.AppointmentRepository;
import org.dci.theratrack.repository.DiagnosisRepository;
import org.dci.theratrack.repository.TreatmentRepository;
import org.dci.theratrack.request.TreatmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

  @Autowired
  private TreatmentRepository treatmentRepository;

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  DiagnosisRepository diagnosisRepository;
  /**
   * Adds a new treatment.
   *
   * @param treatmentRequest the treatment to add
   * @return the added treatment
   * @throws InvalidRequestException if the treatment is null
   */



  // Add treatment method that accepts a TreatmentRequest (DTO)
  public Treatment addTreatment(TreatmentRequest treatmentRequest) {
    if (treatmentRequest == null) {
      throw new InvalidRequestException("Treatment cannot be null.");
    }

    // Create a new Treatment entity
    Treatment treatment = new Treatment();
    treatment.setName(treatmentRequest.getName());
    treatment.setDescription(treatmentRequest.getDescription());
    treatment.setDuration(treatmentRequest.getDuration());
    treatment.setDifficultyLevel(treatmentRequest.getDifficultyLevel());
    treatment.setTreatmentStatus(treatmentRequest.getTreatmentStatus());
    treatment.setNotes(treatmentRequest.getNotes());


    // Optionally, handle diagnosis if diagnosisId is provided
    if (treatmentRequest.getDiagnosisId() != null) {
      Diagnosis diagnosis = diagnosisRepository.findById(treatmentRequest.getDiagnosisId())
          .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with ID: " + treatmentRequest.getDiagnosisId()));
      treatment.setDiagnosis(diagnosis);
    }

    // Save the treatment and return the created entity
    return treatmentRepository.save(treatment);
  }

  // Method to get all treatments
  public List<Treatment> getAllTreatments() {
    return treatmentRepository.findAll();
  }



  /**
   * Retrieves a treatment by its ID.
   *
   * @param id the ID of the treatment
   * @return the treatment
   * @throws ResourceNotFoundException if the treatment is not found
   */
  public Treatment getTreatment(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Treatment ID cannot be null.");
    }
    return treatmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with ID: " + id));
  }

  /**
   * Updates an existing treatment.
   *
   * @param treatment the updated treatment data
   * @return the updated treatment
   * @throws InvalidRequestException if the treatment is null
   * @throws ResourceNotFoundException if the treatment does not exist
   */
  public Treatment updateTreatment(Treatment treatment) {
    if (treatment == null) {
      throw new InvalidRequestException("Treatment cannot be null.");
    }

    if (!treatmentRepository.existsById(treatment.getId())) {
      throw new ResourceNotFoundException("Treatment not found with ID: " + treatment.getId());
    }

    return treatmentRepository.save(treatment);
  }

  /**
   * Deletes a treatment by its ID.
   *
   * @param id the ID of the treatment to delete
   * @throws ResourceNotFoundException if the treatment does not exist
   */
  public void deleteTreatment(Long id) {
    if (!treatmentRepository.existsById(id)) {
      throw new ResourceNotFoundException("Treatment not found with ID: " + id);
    }
    treatmentRepository.deleteById(id);
  }



}
