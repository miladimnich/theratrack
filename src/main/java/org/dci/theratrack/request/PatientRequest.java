package org.dci.theratrack.request;

import jakarta.validation.Valid;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.User;

public class PatientRequest {

    @Valid
    private Patient patient;

    @Valid
    private User user;

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setTPatient(Patient patient) {
        this.patient = patient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
