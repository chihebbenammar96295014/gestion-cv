package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.Competence;
import com.chiedba.candidat.repository.CompetenceRepository;
import com.chiedba.candidat.service.dto.CompetenceDTO;
import com.chiedba.candidat.service.mapper.CompetenceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Competence}.
 */
@Service
@Transactional
public class CompetenceService {

    private final Logger log = LoggerFactory.getLogger(CompetenceService.class);

    private final CompetenceRepository competenceRepository;

    private final CompetenceMapper competenceMapper;

    public CompetenceService(CompetenceRepository competenceRepository, CompetenceMapper competenceMapper) {
        this.competenceRepository = competenceRepository;
        this.competenceMapper = competenceMapper;
    }

    /**
     * Save a competence.
     *
     * @param competenceDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetenceDTO save(CompetenceDTO competenceDTO) {
        log.debug("Request to save Competence : {}", competenceDTO);
        Competence competence = competenceMapper.toEntity(competenceDTO);
        competence = competenceRepository.save(competence);
        return competenceMapper.toDto(competence);
    }

    /**
     * Update a competence.
     *
     * @param competenceDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetenceDTO update(CompetenceDTO competenceDTO) {
        log.debug("Request to update Competence : {}", competenceDTO);
        Competence competence = competenceMapper.toEntity(competenceDTO);
        competence = competenceRepository.save(competence);
        return competenceMapper.toDto(competence);
    }

    /**
     * Partially update a competence.
     *
     * @param competenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompetenceDTO> partialUpdate(CompetenceDTO competenceDTO) {
        log.debug("Request to partially update Competence : {}", competenceDTO);

        return competenceRepository
            .findById(competenceDTO.getId())
            .map(existingCompetence -> {
                competenceMapper.partialUpdate(existingCompetence, competenceDTO);

                return existingCompetence;
            })
            .map(competenceRepository::save)
            .map(competenceMapper::toDto);
    }

    /**
     * Get all the competences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CompetenceDTO> findAll() {
        log.debug("Request to get all Competences");
        return competenceRepository.findAll().stream().map(competenceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one competence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompetenceDTO> findOne(Long id) {
        log.debug("Request to get Competence : {}", id);
        return competenceRepository.findById(id).map(competenceMapper::toDto);
    }

    /**
     * Delete the competence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Competence : {}", id);
        competenceRepository.deleteById(id);
    }
}
