package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.FeedbackTest;
import com.chiedba.candidat.repository.FeedbackTestRepository;
import com.chiedba.candidat.service.dto.FeedbackTestDTO;
import com.chiedba.candidat.service.mapper.FeedbackTestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FeedbackTest}.
 */
@Service
@Transactional
public class FeedbackTestService {

    private final Logger log = LoggerFactory.getLogger(FeedbackTestService.class);

    private final FeedbackTestRepository feedbackTestRepository;

    private final FeedbackTestMapper feedbackTestMapper;

    public FeedbackTestService(FeedbackTestRepository feedbackTestRepository, FeedbackTestMapper feedbackTestMapper) {
        this.feedbackTestRepository = feedbackTestRepository;
        this.feedbackTestMapper = feedbackTestMapper;
    }

    /**
     * Save a feedbackTest.
     *
     * @param feedbackTestDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackTestDTO save(FeedbackTestDTO feedbackTestDTO) {
        log.debug("Request to save FeedbackTest : {}", feedbackTestDTO);
        FeedbackTest feedbackTest = feedbackTestMapper.toEntity(feedbackTestDTO);
        feedbackTest = feedbackTestRepository.save(feedbackTest);
        return feedbackTestMapper.toDto(feedbackTest);
    }

    /**
     * Update a feedbackTest.
     *
     * @param feedbackTestDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackTestDTO update(FeedbackTestDTO feedbackTestDTO) {
        log.debug("Request to update FeedbackTest : {}", feedbackTestDTO);
        FeedbackTest feedbackTest = feedbackTestMapper.toEntity(feedbackTestDTO);
        feedbackTest = feedbackTestRepository.save(feedbackTest);
        return feedbackTestMapper.toDto(feedbackTest);
    }

    /**
     * Partially update a feedbackTest.
     *
     * @param feedbackTestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeedbackTestDTO> partialUpdate(FeedbackTestDTO feedbackTestDTO) {
        log.debug("Request to partially update FeedbackTest : {}", feedbackTestDTO);

        return feedbackTestRepository
            .findById(feedbackTestDTO.getId())
            .map(existingFeedbackTest -> {
                feedbackTestMapper.partialUpdate(existingFeedbackTest, feedbackTestDTO);

                return existingFeedbackTest;
            })
            .map(feedbackTestRepository::save)
            .map(feedbackTestMapper::toDto);
    }

    /**
     * Get all the feedbackTests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedbackTestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FeedbackTests");
        return feedbackTestRepository.findAll(pageable).map(feedbackTestMapper::toDto);
    }

    /**
     * Get one feedbackTest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeedbackTestDTO> findOne(Long id) {
        log.debug("Request to get FeedbackTest : {}", id);
        return feedbackTestRepository.findById(id).map(feedbackTestMapper::toDto);
    }

    /**
     * Delete the feedbackTest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FeedbackTest : {}", id);
        feedbackTestRepository.deleteById(id);
    }
}
