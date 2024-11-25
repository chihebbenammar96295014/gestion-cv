package com.chiedba.candidat.repository;

import com.chiedba.candidat.domain.FeedbackTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackTestRepository extends JpaRepository<FeedbackTest, Long> {}
