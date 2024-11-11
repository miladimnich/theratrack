package org.dci.theratrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

}
