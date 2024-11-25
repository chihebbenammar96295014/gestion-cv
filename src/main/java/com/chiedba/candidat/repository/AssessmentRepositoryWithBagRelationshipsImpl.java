package com.chiedba.candidat.repository;

import com.chiedba.candidat.domain.Assessment;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AssessmentRepositoryWithBagRelationshipsImpl implements AssessmentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Assessment> fetchBagRelationships(Optional<Assessment> assessment) {
        return assessment.map(this::fetchQuestions);
    }

    @Override
    public Page<Assessment> fetchBagRelationships(Page<Assessment> assessments) {
        return new PageImpl<>(fetchBagRelationships(assessments.getContent()), assessments.getPageable(), assessments.getTotalElements());
    }

    @Override
    public List<Assessment> fetchBagRelationships(List<Assessment> assessments) {
        return Optional.of(assessments).map(this::fetchQuestions).orElse(Collections.emptyList());
    }

    Assessment fetchQuestions(Assessment result) {
        return entityManager
            .createQuery(
                "select assessment from Assessment assessment left join fetch assessment.questions where assessment is :assessment",
                Assessment.class
            )
            .setParameter("assessment", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Assessment> fetchQuestions(List<Assessment> assessments) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, assessments.size()).forEach(index -> order.put(assessments.get(index).getId(), index));
        List<Assessment> result = entityManager
            .createQuery(
                "select distinct assessment from Assessment assessment left join fetch assessment.questions where assessment in :assessments",
                Assessment.class
            )
            .setParameter("assessments", assessments)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
