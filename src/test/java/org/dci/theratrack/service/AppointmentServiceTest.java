package org.dci.theratrack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.enums.AppointmentStatus;
import org.dci.theratrack.exceptions.InvalidRequestException;
import org.dci.theratrack.exceptions.ResourceNotFoundException;
import org.dci.theratrack.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

  @Mock
  private AppointmentRepository appointmentRepository;

  @InjectMocks
  private AppointmentService appointmentService;

  private Appointment appointment;
  private Patient patient;
  private Therapist therapist;

  @BeforeEach
  void setUp() {
    appointment = new Appointment();
    appointment.setId(1L);
    appointment.setDateTime("2023-12-01T10:00:00");
    appointment.setSessionDuration(60);
    appointment.setStatus(AppointmentStatus.PENDING);

    patient = new Patient();
    patient.setId(1L);
    patient.setName("John Doe");

    appointment.setTherapist(therapist);
    appointment.setPatient(patient);
    appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
  }

  @Test
  void testCreateAppointment() {
    when(appointmentRepository.save(appointment)).thenReturn(appointment);

    Appointment result = appointmentService.createAppointment(appointment);

    assertNotNull(result);
    assertEquals(appointment.getId(), result.getId());
    assertEquals(appointment.getPatient().getId(), result.getPatient().getId());
    verify(appointmentRepository, times(1)).save(appointment);
  }

  @Test
  void testGetAllAppointments() {
    List<Appointment> appointments = List.of(appointment);

    when(appointmentRepository.findAll()).thenReturn(appointments);

    List<Appointment> result = appointmentService.getAllAppointments();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(appointmentRepository, times(1)).findAll();
  }

  @Test
  void testGetAllAppointmentsEmptyList() {
    when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

    List<Appointment> result = appointmentService.getAllAppointments();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(appointmentRepository, times(1)).findAll();
  }

  @Test
  void testGetAppointment() {
    when(appointmentRepository.findById(1L)).thenReturn(Optional.ofNullable(appointment));

    Appointment result = appointmentService.getAppointment(1L);

    assertNotNull(result);
    assertEquals(appointment.getId(), result.getId());
    verify(appointmentRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenAppointmentIdIsNull() {
    // When & Then
    assertThrows(InvalidRequestException.class, () -> {
      appointmentService.getAppointment(null);
    });

    // Verify no interactions with the repository
    verifyNoInteractions(appointmentRepository);
  }

  @Test
  void testGetAppointmentsByPatient() {
    List<Appointment> appointments = List.of(appointment);

    when(appointmentRepository.getAppointmentsByPatientId(1L)).thenReturn(appointments);

    List<Appointment> result = appointmentService.getAppointmentsByPatient(patient);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(appointmentRepository, times(1)).getAppointmentsByPatientId(1L);
  }

  @Test
  void shouldThrowInvalidRequestExceptionWhenAppointmentIdIsNull() {
    // When & Then
    InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
      appointmentService.getAppointment(null);
    });

    // Assert
    assertEquals("Appointment id cannot be null.", exception.getMessage());

    // Verify no interactions with the repository
    verifyNoInteractions(appointmentRepository);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenAppointmentIdDoesNotExist() {
    // Given
    Long nonExistentId = 999L;
    when(appointmentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // When & Then
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      appointmentService.getAppointment(nonExistentId);
    });

    // Assert
    assertEquals("Appointment not found with ID: " + nonExistentId, exception.getMessage());
    verify(appointmentRepository, times(1)).findById(nonExistentId);
  }

  @Test
  void testGetAppointmentsByPatientNoAppointments(){
    when(appointmentRepository.getAppointmentsByPatientId(1L)).thenReturn(Collections.emptyList());

    List<Appointment> result = appointmentService.getAppointmentsByPatient(patient);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(appointmentRepository, times(1)).getAppointmentsByPatientId(1L);
  }

  @Test
  void testUpdateAppointment() {
    // Arrange: Set up a valid appointment
    appointment.setId(1L);
    when(appointmentRepository.existsById(1L)).thenReturn(true);
    when(appointmentRepository.save(appointment)).thenReturn(appointment);

    // Act: Call the service method
    Appointment result = appointmentService.updateAppointment(appointment);

    // Assert: Verify the result
    assertNotNull(result);
    assertEquals(appointment, result);
    verify(appointmentRepository, times(1)).existsById(1L);
    verify(appointmentRepository, times(1)).save(appointment);
  }

  @Test
  void testUpdateAppointmentInvalidRequestNullAppointment() {
    // Act & Assert: Call the service method with null and expect an exception
    InvalidRequestException exception = assertThrows(
        InvalidRequestException.class,
        () -> appointmentService.updateAppointment(null)
    );

    // Verify the exception message
    assertEquals("Appointment or appointment ID cannot be null.", exception.getMessage());

    // Verify no repository interaction occurs
    verifyNoInteractions(appointmentRepository);
  }

  @Test
  void testUpdateAppointmentInvalidRequestNullId() {
    // Arrange: Set up an appointment with null ID
    appointment.setId(null);

    // Act & Assert: Call the service method and expect an exception
    InvalidRequestException exception = assertThrows(
        InvalidRequestException.class,
        () -> appointmentService.updateAppointment(appointment)
    );

    // Verify the exception message
    assertEquals("Appointment or appointment ID cannot be null.", exception.getMessage());

    // Verify no repository interaction occurs
    verifyNoInteractions(appointmentRepository);
  }

  @Test
  void testUpdateAppointmentNotFound() {
    // Arrange: Simulate the appointment ID not existing
    appointment.setId(1L);
    when(appointmentRepository.existsById(1L)).thenReturn(false);

    // Act & Assert: Call the service method and expect an exception
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> appointmentService.updateAppointment(appointment)
    );

    // Verify the exception message
    assertEquals("Appointment not found with ID: 1", exception.getMessage());

    // Verify repository interaction
    verify(appointmentRepository, times(1)).existsById(1L);
    verify(appointmentRepository, never()).save(any());
  }

  @Test
  void testDeleteAppointment() {

    when(appointmentRepository.existsById(1L)).thenReturn(true);
    doNothing().when(appointmentRepository).deleteById(1L);

    appointmentService.deleteAppointment(1L);

    verify(appointmentRepository, times(1)).deleteById(1L);
  }

  @Test
  void testDeleteAppointmentNotFound() {
    // Arrange: Simulate the appointment ID does not exist
    Long appointmentId = 1L;
    when(appointmentRepository.existsById(appointmentId)).thenReturn(false);

    // Act & Assert: Expect a ResourceNotFoundException
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> appointmentService.deleteAppointment(appointmentId)
    );

    // Verify the exception message
    assertEquals("Appointment not found with ID: " + appointmentId, exception.getMessage());

    // Verify repository interaction
    verify(appointmentRepository, times(1)).existsById(appointmentId);
    verify(appointmentRepository, never()).deleteById(any());
  }

  @Test
  void testDeleteAppointmentInvalidRequestNullId() {
    // Act & Assert: Expect an exception for null appointment ID
    InvalidRequestException exception = assertThrows(
        InvalidRequestException.class,
        () -> appointmentService.deleteAppointment(null)
    );

    // Verify the exception message
    assertEquals("Appointment ID cannot be null.", exception.getMessage());

    // Verify no repository interaction
    verifyNoInteractions(appointmentRepository);
  }
}