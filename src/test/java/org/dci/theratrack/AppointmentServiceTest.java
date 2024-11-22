package org.dci.theratrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.enums.AppointmentStatus;
import org.dci.theratrack.repository.AppointmentRepository;
import org.dci.theratrack.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

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
    appointment.setDateTime(LocalDateTime.of(2023, 12, 1, 10, 0)); // Correct LocalDateTime format
    appointment.setSessionDuration(60);
    appointment.setStatus(AppointmentStatus.PENDING);

    // Initialize the Patient object
    patient = new Patient();
    patient.setId(1L);
    patient.setName("John Doe");

    // Initialize the Therapist object
    therapist = new Therapist();
    therapist.setId(1L);
    therapist.setName("Jane Smith");

    // Assign Therapist and Patient to the appointment
    appointment.setTherapist(therapist);
    appointment.setPatient(patient);
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
     when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

     Appointment result = appointmentService.getAppointment(1L);

     assertNotNull(result, "The appointment should not be null");
    assertEquals(appointment.getId(), result.getId(), "The appointment ID should match");
    assertEquals(appointment.getDateTime(), result.getDateTime(), "The appointment dateTime should match");
    assertEquals(appointment.getSessionDuration(), result.getSessionDuration(), "The appointment duration should match");
    assertEquals(appointment.getStatus(), result.getStatus(), "The appointment status should match");
    assertEquals(appointment.getPatient().getId(), result.getPatient().getId(), "The appointment patient should match");
    assertEquals(appointment.getTherapist().getId(), result.getTherapist().getId(), "The appointment therapist should match");

     verify(appointmentRepository, times(1)).findById(1L);
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
  void testGetAppointmentsByPatientNoAppointments() {
    when(appointmentRepository.getAppointmentsByPatientId(1L)).thenReturn(Collections.emptyList());

    List<Appointment> result = appointmentService.getAppointmentsByPatient(patient);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(appointmentRepository, times(1)).getAppointmentsByPatientId(1L);
  }

  @Test
  void testUpdateAppointment() {
    appointment.setSessionDuration(90); // Change session duration
    when(appointmentRepository.existsById(appointment.getId())).thenReturn(true);
    when(appointmentRepository.save(appointment)).thenReturn(appointment);

    Appointment result = appointmentService.updateAppointment(appointment);

    assertNotNull(result);
    assertEquals(90, result.getSessionDuration());
    verify(appointmentRepository, times(1)).existsById(
        appointment.getId());  // Verify existsById was called
    verify(appointmentRepository, times(1)).save(appointment);  // Verify save was called
  }


  @Test
  void testDeleteAppointment() {
    Long appointmentId = 1L;

    when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
    doNothing().when(appointmentRepository).deleteById(appointmentId);

    appointmentService.deleteAppointment(appointmentId);

    verify(appointmentRepository, times(1)).existsById(appointmentId);
    verify(appointmentRepository, times(1)).deleteById(appointmentId);
  }

}