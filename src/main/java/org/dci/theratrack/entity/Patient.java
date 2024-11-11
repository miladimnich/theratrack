package org.dci.theratrack.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "patients")
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String surname;
  private String email;
  private String phone;
  private String gender;
  private String birthDate;
  private String address;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "patients_therapists",
      joinColumns = @JoinColumn(name = "patient_id"),
      inverseJoinColumns = @JoinColumn(name = "therapist_id")
  )
  private List<Therapist> therapists;


  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Diagnosis> diagnoses;


  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Appointment appointment;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;
}