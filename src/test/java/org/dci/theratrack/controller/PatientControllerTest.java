package org.dci.theratrack.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.dci.theratrack.config.TestSecurityConfig;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.enums.AppointmentStatus;
import org.dci.theratrack.repository.AppointmentRepository;
import org.dci.theratrack.repository.PatientRepository;
import org.dci.theratrack.request.PatientRequest;
import org.dci.theratrack.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TestSecurityConfig.class, PatientController.class})
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientService patientService;

  @MockBean
  private PatientRepository patientRepository;

  @BeforeEach
  void setup() {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John");
    patient.setSurname("Doe");
    patientRepository.save(patient);
  }

  @Test
  void testGetAllPatients() throws Exception {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John");
    patient.setSurname("Doe");

    when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient));

    mockMvc.perform(get("/api/patients"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("John"))
        .andExpect(jsonPath("$[0].surname").value("Doe"));

    verify(patientService, times(1)).getAllPatients();
  }

  @Test
  void testGetPatientById() throws Exception {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John");
    patient.setSurname("Doe");

    when(patientService.getPatientById(1L)).thenReturn(patient);

    mockMvc.perform(get("/api/patients/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.surname").value("Doe"));

    verify(patientService, times(1)).getPatientById(1L);
  }

  @Test
  void testUpdatePatient() throws Exception {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("Updated Name");
    patient.setSurname("Updated Surname");

    when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

    String patientJson = "{ \"name\": \"Updated Name\", \"surname\": \"Updated Surname\" }";

    mockMvc.perform(put("/api/patients/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.surname").value("Updated Surname"));

    verify(patientService, times(1)).updatePatient(eq(1L), any(Patient.class));
  }

  @Test
  void testDeletePatient() throws Exception {
    doNothing().when(patientService).deletePatient(1L);

    mockMvc.perform(delete("/api/patients/1"))
        .andExpect(status().isNoContent());

    verify(patientService, times(1)).deletePatient(1L);
  }

  @Test
  void testCreatePatient() throws Exception {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John");
    patient.setSurname("Doe");

    when(patientService.createPatient(any(PatientRequest.class))).thenReturn(patient);

    String patientJson = "{ \"name\": \"John\", \"surname\": \"Doe\" }";

    mockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.surname").value("Doe"));

    verify(patientService, times(1)).createPatient(any(PatientRequest.class));
  }

  @Test
  void testGetPatientTherapyHistory_found() throws Exception {
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John");
    patient.setSurname("Doe");

    Appointment appointment = new Appointment();
    appointment.setId(1L);
    appointment.setPatient(patient);
    appointment.setSessionDuration(60);
    appointment.setStatus(AppointmentStatus.COMPLETED);
    appointment.setDateTime(LocalDateTime.now());
    appointment.setTherapist(new Therapist());

    // Mock the service layer to return a list with the mock appointment
    when(patientService.getPatientTherapyHistory(1L)).thenReturn(List.of(appointment));

     mockMvc.perform(
            get("/api/patients/1/therapy-history"))
        .andExpect(status().isOk())  // Expect HTTP status 200 OK
        .andExpect(jsonPath("$[0].id").value(1))  // Check if the ID is 1
        .andExpect(jsonPath("$[0].patient.id").value(1))  // Check the patient ID
        .andExpect(jsonPath("$[0].status").value("COMPLETED"));  // Check the status

    // Then: Verify the service was called exactly once with the correct patient ID
    verify(patientService, times(1)).getPatientTherapyHistory(1L);
  }

  @Test
  void testGetPatientTherapyHistory_notFound() throws Exception {
    when(patientService.getPatientTherapyHistory(1L)).thenReturn(List.of());

    mockMvc.perform(get("/api/patients/1/therapy-history"))
        .andExpect(status().isNotFound());  // Expect HTTP status 404

    // Then: Verify the service was called exactly once with the correct patient ID
    verify(patientService, times(1)).getPatientTherapyHistory(1L);
  }


}

