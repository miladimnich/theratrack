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
import java.util.Optional;
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
    when(diagnosisRepository.findById(1L)).thenReturn(Optional.of(diagnosis));

    Diagnosis result = diagnosisService.getDiagnosis(1L);

    // Then: Verify the result is not null and matches the expected diagnosis
    assertNotNull(result, "The diagnosis should not be null");
    assertEquals(diagnosis.getId(), result.getId(), "The diagnosis ID should match");
    assertEquals(diagnosis.getName(), result.getName(), "The diagnosis name should match");
    assertEquals(diagnosis.getDescription(), result.getDescription(), "The diagnosis description should match");

    // Verify that the repository's findById method was called once with the correct ID
    verify(diagnosisRepository, times(1)).findById(1L);
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
    when(diagnosisRepository.existsById(diagnosis.getId())).thenReturn(true);

    when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);

    Diagnosis result = diagnosisService.updateDiagnosis(diagnosis);

    assertNotNull(result);
    assertEquals("Updated Diagnosis", result.getName());
    verify(diagnosisRepository, times(1)).save(diagnosis);
  }

  @Test
  void testDeleteDiagnosis() {
    when(diagnosisRepository.existsById(1L)).thenReturn(true);
    doNothing().when(diagnosisRepository).deleteById(1L); // Mock deleteById to do nothing

    // Call the deleteDiagnosis method
    diagnosisService.deleteDiagnosis(1L);

    // Verify that deleteById was called once with the correct argument (1L)
    verify(diagnosisRepository, times(1)).deleteById(1L);

  }
}
