package org.dci.theratrack.request;

import jakarta.validation.Valid;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.entity.User;

public class TherapistRequest {

    @Valid
    private Therapist therapist;

    @Valid
    private User user;

    // Getters and Setters
    public Therapist getTherapist() {
        return therapist;
    }

    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
