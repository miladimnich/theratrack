package org.dci.theratrack.service;

import java.util.List;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

  @Autowired
  private TreatmentRepository treatmentRepository;

  public Treatment addTreatment(Treatment treatment) {
    return treatmentRepository.save(treatment);
  }

  public List<Treatment> getAllTreatments() {
    return treatmentRepository.findAll();
  }

  public Treatment getTreatment(Long id) {
    return treatmentRepository.getReferenceById(id);
  }

  public Treatment updateTreatment(Treatment treatment) {
    return treatmentRepository.save(treatment);
  }

  public void deleteTreatment(Long id) {
    treatmentRepository.deleteById(id);
  }

}
