package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.repository.ResumeRepository;
import com.chiedba.candidat.service.dto.ResumeDTO;
import com.chiedba.candidat.service.mapper.ResumeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resume}.
 */
@Service
@Transactional
public class ResumeService {

    private final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;

    private final ResumeMapper resumeMapper;

    public ResumeService(ResumeRepository resumeRepository, ResumeMapper resumeMapper) {
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
    }

    /**
     * Save a resume.
     *
     * @param resumeDTO the entity to save.
     * @return the persisted entity.
     */
    public ResumeDTO save(ResumeDTO resumeDTO) {
        log.debug("Request to save Resume : {}", resumeDTO);
        Resume resume = resumeMapper.toEntity(resumeDTO);
        resume = resumeRepository.save(resume);
        return resumeMapper.toDto(resume);
    }

    /**
     * Update a resume.
     *
     * @param resumeDTO the entity to save.
     * @return the persisted entity.
     */
    public ResumeDTO update(ResumeDTO resumeDTO) {
        log.debug("Request to update Resume : {}", resumeDTO);
        Resume resume = resumeMapper.toEntity(resumeDTO);
        resume = resumeRepository.save(resume);
        return resumeMapper.toDto(resume);
    }

    /**
     * Partially update a resume.
     *
     * @param resumeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResumeDTO> partialUpdate(ResumeDTO resumeDTO) {
        log.debug("Request to partially update Resume : {}", resumeDTO);

        return resumeRepository
            .findById(resumeDTO.getId())
            .map(existingResume -> {
                resumeMapper.partialUpdate(existingResume, resumeDTO);

                return existingResume;
            })
            .map(resumeRepository::save)
            .map(resumeMapper::toDto);
    }

    /**
     * Get all the resumes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ResumeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resumes");
        return resumeRepository.findAll(pageable).map(resumeMapper::toDto);
    }

    /**
     *  Get all the resumes where AppUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResumeDTO> findAllWhereAppUserIsNull() {
        log.debug("Request to get all resumes where AppUser is null");
        return StreamSupport
            .stream(resumeRepository.findAll().spliterator(), false)
            .filter(resume -> resume.getAppUser() == null)
            .map(resumeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one resume by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResumeDTO> findOne(Long id) {
        log.debug("Request to get Resume : {}", id);
        return resumeRepository.findById(id).map(resumeMapper::toDto);
    }

    /**
     * Delete the resume by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resume : {}", id);
        resumeRepository.deleteById(id);
    }
}
