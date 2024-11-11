package org.dci.theratrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Treatment extends JpaRepository<Treatment, Integer> {

}
