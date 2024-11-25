package com.chiedba.candidat.repository;

import com.chiedba.candidat.domain.ExperiencePro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExperiencePro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperienceProRepository extends JpaRepository<ExperiencePro, Long> {}
