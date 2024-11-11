package org.dci.theratrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.enums.TreatmentStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="treatments")
public class Treatment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String duration;
  private DifficultyLevel difficultyLevel;
  private TreatmentStatus treatmentStatus;

  @OneToOne
  @JoinColumn(name = "diagnosis_id", unique = true)
  private Diagnosis diagnosis;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "appointment_id") // Foreign key to appointments table
  private Appointment appointment;

}
