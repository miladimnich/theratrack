package org.dci.theratrack.service;

import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.exceptions.InvalidRequestException;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private ModelMapper modelMapper;


  /**
   * Creates a new patient.
   *
   * @param patient the patient to create
   * @return the created patient
   * @throws InvalidRequestException if the patient is null
   */
  public Patient createPatient(Patient patient) {
    if (patient == null) {
      throw new InvalidRequestException("Patient cannot be null.");
    }
    return patientRepository.save(patient);
  }

  public List<Patient> getAllPatients() {
    return patientRepository.findAll();
  }

  /**
   * Retrieves a patient by ID.
   *
   * @param id the ID of the patient
   * @return the patient
   * @throws ResourceNotFoundException if the patient is not found
   */
  public Patient getPatientById(Long id) {
    return patientRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
  }

  /**
   * Updates an existing patient.
   *
   * @param id the ID of the patient to update
   * @param updatedPatient the updated patient data
   * @return the updated patient
   * @throws ResourceNotFoundException if the patient is not found
   * @throws InvalidRequestException if the updated patient is null
   */
  public Patient updatePatient(Long id, Patient updatedPatient) {
    if (updatedPatient == null) {
      throw new InvalidRequestException("Updated patient cannot be null.");
    }

    Patient existingPatient = patientRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));

    modelMapper.typeMap(Patient.class, Patient.class).addMappings(mapper -> mapper.skip(Patient::setId));
    modelMapper.map(updatedPatient, existingPatient);

    return patientRepository.save(existingPatient);
  }

  /**
   * Deletes a patient by ID.
   *
   * @param id the ID of the patient to delete
   * @throws ResourceNotFoundException if the patient does not exist
   */
  public void deletePatient(Long id) {
    if (!patientRepository.existsById(id)) {
      throw new ResourceNotFoundException("Patient not found with ID: " + id);
    }
    patientRepository.deleteById(id);
  }
}