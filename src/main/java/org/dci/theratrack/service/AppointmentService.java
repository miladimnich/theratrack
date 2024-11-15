package org.dci.theratrack.service;

import java.util.List;
import org.dci.theratrack.entity.Appointment;
import org.dci.theratrack.entity.Patient;
import org.dci.theratrack.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

  @Autowired
  private AppointmentRepository appointmentRepository;

  public Appointment createAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }

  public Appointment getAppointment(Long appointmentId) {
    return appointmentRepository.getReferenceById(appointmentId);
  }

  public List<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  public List<Appointment> getAppointmentsByPatient(Patient patient) {
    return appointmentRepository.getAppointmentsByPatientId(patient.getId());
  }

  public Appointment updateAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }

  public void deleteAppointment(Long appointmentId) {
    appointmentRepository.deleteById(appointmentId);
  }
}
