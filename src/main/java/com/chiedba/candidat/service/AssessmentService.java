package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.Assessment;
import com.chiedba.candidat.repository.AssessmentRepository;
import com.chiedba.candidat.service.dto.AssessmentDTO;
import com.chiedba.candidat.service.mapper.AssessmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assessment}.
 */
@Service
@Transactional
public class AssessmentService {

    private final Logger log = LoggerFactory.getLogger(AssessmentService.class);

    private final AssessmentRepository assessmentRepository;

    private final AssessmentMapper assessmentMapper;

    public AssessmentService(AssessmentRepository assessmentRepository, AssessmentMapper assessmentMapper) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentMapper = assessmentMapper;
    }

    /**
     * Save a assessment.
     *
     * @param assessmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AssessmentDTO save(AssessmentDTO assessmentDTO) {
        log.debug("Request to save Assessment : {}", assessmentDTO);
        Assessment assessment = assessmentMapper.toEntity(assessmentDTO);
        assessment = assessmentRepository.save(assessment);
        return assessmentMapper.toDto(assessment);
    }

    /**
     * Update a assessment.
     *
     * @param assessmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AssessmentDTO update(AssessmentDTO assessmentDTO) {
        log.debug("Request to update Assessment : {}", assessmentDTO);
        Assessment assessment = assessmentMapper.toEntity(assessmentDTO);
        assessment = assessmentRepository.save(assessment);
        return assessmentMapper.toDto(assessment);
    }

    /**
     * Partially update a assessment.
     *
     * @param assessmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssessmentDTO> partialUpdate(AssessmentDTO assessmentDTO) {
        log.debug("Request to partially update Assessment : {}", assessmentDTO);

        return assessmentRepository
            .findById(assessmentDTO.getId())
            .map(existingAssessment -> {
                assessmentMapper.partialUpdate(existingAssessment, assessmentDTO);

                return existingAssessment;
            })
            .map(assessmentRepository::save)
            .map(assessmentMapper::toDto);
    }

    /**
     * Get all the assessments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssessmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assessments");
        return assessmentRepository.findAll(pageable).map(assessmentMapper::toDto);
    }

    /**
     * Get all the assessments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AssessmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assessmentRepository.findAllWithEagerRelationships(pageable).map(assessmentMapper::toDto);
    }

    /**
     * Get one assessment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssessmentDTO> findOne(Long id) {
        log.debug("Request to get Assessment : {}", id);
        return assessmentRepository.findOneWithEagerRelationships(id).map(assessmentMapper::toDto);
    }

    /**
     * Delete the assessment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Assessment : {}", id);
        assessmentRepository.deleteById(id);
    }
}
