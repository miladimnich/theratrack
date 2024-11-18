package org.dci.theratrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.dci.theratrack.entity.Diagnosis;
import org.dci.theratrack.repository.DiagnosisRepository;
import org.dci.theratrack.service.DiagnosisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DiagnosisServiceTest {

  @Mock
  private DiagnosisRepository diagnosisRepository;

  @InjectMocks
  private DiagnosisService diagnosisService;

  private Diagnosis diagnosis;

  @BeforeEach
  void setUp() {
    diagnosis = new Diagnosis();
    diagnosis.setId(1L);
    diagnosis.setName("Diabetes");
    diagnosis.setDescription(
        "A chronic condition that affects how the body processes blood sugar.");
  }

  @Test
  void testGetDiagnosis() {
    when(diagnosisRepository.getReferenceById(1L)).thenReturn(diagnosis);

    Diagnosis result = diagnosisService.getDiagnosis(1L);

    assertNotNull(result);
    assertEquals(diagnosis.getId(), result.getId());
    assertEquals(diagnosis.getName(), result.getName());
    verify(diagnosisRepository, times(1)).getReferenceById(1L);
  }

  @Test
  void testGetDiagnoses() {
    List<Diagnosis> diagnoses = List.of(diagnosis);

    when(diagnosisRepository.findAll()).thenReturn(diagnoses);

    List<Diagnosis> result = diagnosisService.getDiagnoses();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(diagnosis.getId(), result.get(0).getId());
    verify(diagnosisRepository, times(1)).findAll();
  }

  @Test
  void testGetDiagnosesEmptyList() {
    when(diagnosisRepository.findAll()).thenReturn(Collections.emptyList());

    List<Diagnosis> result = diagnosisService.getDiagnoses();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(diagnosisRepository, times(1)).findAll();
  }

  @Test
  void testAddDiagnosis() {
    when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);

    Diagnosis result = diagnosisService.addDiagnosis(diagnosis);

    assertNotNull(result);
    assertEquals(diagnosis.getId(), result.getId());
    assertEquals(diagnosis.getName(), result.getName());
    verify(diagnosisRepository, times(1)).save(diagnosis);
  }

  @Test
  void testUpdateDiagnosis() {
    diagnosis.setName("Updated Diagnosis");

    when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);

    Diagnosis result = diagnosisService.updateDiagnosis(diagnosis);

    assertNotNull(result);
    assertEquals("Updated Diagnosis", result.getName());
    verify(diagnosisRepository, times(1)).save(diagnosis);
  }

  @Test
  void testDeleteDiagnosis() {
    doNothing().when(diagnosisRepository).deleteById(1L);

    diagnosisService.deleteDiagnosis(1L);

    verify(diagnosisRepository, times(1)).deleteById(1L);
  }
}
