package org.dci.theratrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="therapists")
public class Therapist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String surname;
  private String email;
  private String phone;
  private String gender;
  private String birthDate;

  @ManyToMany(mappedBy = "therapists")
  private List<Patient> patients;

  @OneToOne
  @JoinColumn(name = "appointment_id")
  private Appointment appointment;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;

}
