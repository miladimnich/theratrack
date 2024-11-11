package org.dci.theratrack.enums;

public enum AppointmentStatus {
  SCHEDULED,   // Appointment is scheduled but not yet confirmed
  CONFIRMED,   // Appointment is confirmed by the user or provider
  CANCELED,    // Appointment has been canceled by the user or provider
  COMPLETED,   // Appointment has been completed
  RESCHEDULED, // Appointment was rescheduled to a different time
  NO_SHOW,     // User did not show up for the appointment
  PENDING      // Appointment is pending confirmation
}
