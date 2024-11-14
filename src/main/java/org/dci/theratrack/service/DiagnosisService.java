package org.dci.theratrack.service;

import java.util.List;
import org.dci.theratrack.entity.Diagnosis;
import org.dci.theratrack.repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisService {

  @Autowired
  private DiagnosisRepository diagnosisRepository;

  public Diagnosis getDiagnosis(Long diagnosisId) {
    return diagnosisRepository.getReferenceById(diagnosisId);
  }

  public List<Diagnosis> getDiagnoses() {
    return diagnosisRepository.findAll();
  }

  public Diagnosis addDiagnosis(Diagnosis diagnosis) {
    return diagnosisRepository.save(diagnosis);
  }

  public Diagnosis updateDiagnosis(Diagnosis diagnosis) {
    return diagnosisRepository.save(diagnosis);
  }

  public void deleteDiagnosis(Long diagnosisId) {
    diagnosisRepository.deleteById(diagnosisId);
  }
}
