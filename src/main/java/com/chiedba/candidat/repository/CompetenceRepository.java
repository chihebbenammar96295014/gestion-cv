package com.chiedba.candidat.repository;

import com.chiedba.candidat.domain.Competence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Competence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long> {}
