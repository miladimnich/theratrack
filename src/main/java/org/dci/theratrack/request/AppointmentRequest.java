package org.dci.theratrack.request;


import jakarta.validation.Valid;
import java.util.List;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.entity.Treatment;

public class AppointmentRequest {

  @Valid
  private Appointment appointment;

  @Valid
  private Patient patient;

  @Valid
  private Therapist therapist;


  @Valid
  private TreatmentRequest treatment;

  public TreatmentRequest getTreatment() {
    return treatment;
  }

  public void setTreatment(TreatmentRequest treatment) {
    this.treatment = treatment;
  }


  public Appointment getAppointment() {
    return appointment;
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public Therapist getTherapist() {
    return therapist;
  }

  public void setTherapist(Therapist therapist) {
    this.therapist = therapist;
  }
}