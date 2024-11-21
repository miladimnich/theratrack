package org.dci.theratrack.enums;

public enum TreatmentStatus {
  PLANNED,        // The treatment session is scheduled but has not started yet
  IN_PROGRESS,    // The treatment session is currently ongoing
  COMPLETED,      // The treatment session is finished successfully
  CANCELLED,      // The session was cancelled by the patient or therapist
  POSTPONED,      // The session was delayed and rescheduled
  ON_HOLD,        // The treatment is temporarily paused (e.g., due to patient condition)
  FAILED          // The treatment session was interrupted or ended unsuccessfully
}
