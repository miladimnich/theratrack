package org.dci.theratrack.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.enums.TreatmentStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "treatments")
public class Treatment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Treatment name must not be blank")
  @Column(nullable = false)
  private String name;

  @Size(max = 1000)
  private String description;

  @Min(1)  // Enforcing positive duration
  @Column(nullable = false)
  private Integer duration;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DifficultyLevel difficultyLevel;


  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TreatmentStatus treatmentStatus;

  @OneToOne
  @JoinColumn(name = "diagnosis_id", unique = true)
  private Diagnosis diagnosis;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "appointment_id")
  @JsonBackReference // Prevents recursion from the other side
  private Appointment appointment;

  @Column(length = 2000)
  private String notes;



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public DifficultyLevel getDifficultyLevel() {
    return difficultyLevel;
  }

  public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
    this.difficultyLevel = difficultyLevel;
  }

  public TreatmentStatus getTreatmentStatus() {
    return treatmentStatus;
  }

  public void setTreatmentStatus(TreatmentStatus treatmentStatus) {
    this.treatmentStatus = treatmentStatus;
  }

  public Diagnosis getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(Diagnosis diagnosis) {
    this.diagnosis = diagnosis;
  }

  public Appointment getAppointment() {
    return appointment;
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
