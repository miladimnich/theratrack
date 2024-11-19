package org.dci.theratrack;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.dci.theratrack.entity.Treatment;
import org.dci.theratrack.repository.TreatmentRepository;
import org.dci.theratrack.service.TreatmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class TreatmentServiceTest {

  @Mock
  private TreatmentRepository treatmentRepository;

  @InjectMocks
  private TreatmentService treatmentService;

  private Treatment treatment;

  @BeforeEach
  void setUp() {
    treatment = new Treatment();
    treatment.setId(1L);
    treatment.setName("Insulin");
    treatment.setDescription("Diabetes treatment");
  }

  @Test
  void testAddTreatment() {
    when(treatmentRepository.save(any(Treatment.class))).thenReturn(treatment);

    Treatment savedTreatment = treatmentService.addTreatment(treatment);

    assertNotNull(savedTreatment);
    assertEquals(treatment.getId(), savedTreatment.getId());
    assertEquals(treatment.getName(), savedTreatment.getName());
    verify(treatmentRepository, times(1)).save(treatment);
  }

  @Test
  void testGetAllTreatments() {
    List<Treatment> treatments = List.of(treatment);

    when(treatmentRepository.findAll()).thenReturn(treatments);

    List<Treatment> result = treatmentService.getAllTreatments();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(treatmentRepository, times(1)).findAll();
  }

  @Test
  void testGetAllTreatmentsEmptyList() {
    when(treatmentRepository.findAll()).thenReturn(Collections.emptyList());

    List<Treatment> result = treatmentService.getAllTreatments();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(treatmentRepository, times(1)).findAll();
  }

  @Test
  void testGetTreatment() {
    when(treatmentRepository.getReferenceById(1L)).thenReturn(treatment);

    Treatment result = treatmentService.getTreatment(1L);

    assertNotNull(result);
    assertEquals(treatment.getId(), result.getId());
    verify(treatmentRepository, times(1)).getReferenceById(1L);
  }

  @Test
  void testUpdateTreatment() {
    treatment.setDescription("Updated description");

    when(treatmentRepository.save(treatment)).thenReturn(treatment);

    Treatment result = treatmentService.updateTreatment(treatment);

    assertNotNull(result);
    assertEquals("Updated description", result.getDescription());
    verify(treatmentRepository, times(1)).save(treatment);
  }

  @Test
  void testdeleteTreatment() {
    doNothing().when(treatmentRepository).deleteById(1L);

    treatmentService.deleteTreatment(1L);

    verify(treatmentRepository, times(1)).deleteById(1L);  }
}

