package org.dci.theratrack.controller;

import org.dci.theratrack.config.TestSecurityConfig;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.repository.TherapistRepository;
import org.dci.theratrack.service.TherapistService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {TestSecurityConfig.class, TherapistController.class})
@WebMvcTest(TherapistController.class)
public class TherapistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TherapistService therapistService;

    @MockBean
    private TherapistRepository therapistRepository;

    @BeforeEach
    void setup() {
        Therapist therapist = new Therapist();
        therapist.setId(1L);
        therapist.setName("John");
        therapist.setSurname("Doe");
        therapistRepository.save(therapist);
    }

    @Test
    void testGetAllTherapists() throws Exception {
        Therapist therapist = new Therapist();
        therapist.setId(1L);
        therapist.setName("John");
        therapist.setSurname("Doe");

        when(therapistService.getAllTherapists()).thenReturn(Arrays.asList(therapist));

        mockMvc.perform(get("/api/therapists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].surname").value("Doe"));

        verify(therapistService, times(1)).getAllTherapists();
    }

    @Test
    void testGetTherapistById() throws Exception {
        Therapist therapist = new Therapist();
        therapist.setId(1L);
        therapist.setName("John");
        therapist.setSurname("Doe");

        when(therapistService.getTherapistById(1L)).thenReturn(therapist);

        mockMvc.perform(get("/api/therapists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(therapistService, times(1)).getTherapistById(1L);
    }

    @Test
    void testUpdateTherapist() throws Exception {
        Therapist therapist = new Therapist();
        therapist.setId(1L);
        therapist.setName("Updated Name");
        therapist.setSurname("Updated Surname");

        when(therapistService.updateTherapist(eq(1L), any(Therapist.class))).thenReturn(therapist);

        String therapistJson = "{ \"name\": \"Updated Name\", \"surname\": \"Updated Surname\" }";

        mockMvc.perform(put("/api/therapists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(therapistJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.surname").value("Updated Surname"));

        verify(therapistService, times(1)).updateTherapist(eq(1L), any(Therapist.class));
    }

    @Test
    void testDeleteTherapist() throws Exception {
        doNothing().when(therapistService).deleteTherapist(1L);

        mockMvc.perform(delete("/api/therapists/1"))
                .andExpect(status().isNoContent());

        verify(therapistService, times(1)).deleteTherapist(1L);
    }

    @Test
    void testCreateTherapist() throws Exception {
        Therapist therapist = new Therapist();
        therapist.setId(1L);
        therapist.setName("John");
        therapist.setSurname("Doe");

        when(therapistService.createTherapist(any(Therapist.class))).thenReturn(therapist);

        String therapistJson = "{ \"name\": \"John\", \"surname\": \"Doe\" }";

        mockMvc.perform(post("/api/therapists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(therapistJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(therapistService, times(1)).createTherapist(any(Therapist.class));
    }
}

