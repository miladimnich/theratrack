package org.dci.theratrack.controller;

import org.dci.theratrack.config.TestSecurityConfig;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.repository.PatientRepository;
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

        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        String patientJson = "{ \"name\": \"John\", \"surname\": \"Doe\" }";

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(patientService, times(1)).createPatient(any(Patient.class));
    }
}

