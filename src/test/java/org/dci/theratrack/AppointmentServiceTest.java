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
    when(appointmentRepository.getReferenceById(1L)).thenReturn(appointment);

    Appointment result = appointmentService.getAppointment(1L);

    assertNotNull(result);
    assertEquals(appointment.getId(), result.getId());
    verify(appointmentRepository, times(1)).getReferenceById(1L);
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
  void testGetAppointmentsByPatientNoAppointments(){
    when(appointmentRepository.getAppointmentsByPatientId(1L)).thenReturn(Collections.emptyList());

    List<Appointment> result = appointmentService.getAppointmentsByPatient(patient);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(appointmentRepository, times(1)).getAppointmentsByPatientId(1L);
  }

  @Test
  void testUpdateAppointment(){
    appointment.setSessionDuration(90);

    when(appointmentRepository.save(appointment)).thenReturn(appointment);

    Appointment result = appointmentService.updateAppointment(appointment);

    assertNotNull(result);
    assertEquals(90, result.getSessionDuration());
    verify(appointmentRepository, times(1)).save(appointment);
  }

  @Test
  void testDeleteAppointment() {
    doNothing().when(appointmentRepository).deleteById(1L);

    appointmentService.deleteAppointment(1L);

    verify(appointmentRepository, times(1)).deleteById(1L);
  }
}