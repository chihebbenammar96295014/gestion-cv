package com.chiedba.candidat.repository;

import com.chiedba.candidat.domain.Reponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {}
