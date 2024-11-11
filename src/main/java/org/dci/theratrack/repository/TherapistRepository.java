package org.dci.theratrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.dci.theratrack.entity.Therapist;

public interface TherapistRepository extends JpaRepository<Therapist, Long>{
}
