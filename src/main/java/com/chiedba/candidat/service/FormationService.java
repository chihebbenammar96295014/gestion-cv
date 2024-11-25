package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.Formation;
import com.chiedba.candidat.repository.FormationRepository;
import com.chiedba.candidat.service.dto.FormationDTO;
import com.chiedba.candidat.service.mapper.FormationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Formation}.
 */
@Service
@Transactional
public class FormationService {

    private final Logger log = LoggerFactory.getLogger(FormationService.class);

    private final FormationRepository formationRepository;

    private final FormationMapper formationMapper;

    public FormationService(FormationRepository formationRepository, FormationMapper formationMapper) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
    }

    /**
     * Save a formation.
     *
     * @param formationDTO the entity to save.
     * @return the persisted entity.
     */
    public FormationDTO save(FormationDTO formationDTO) {
        log.debug("Request to save Formation : {}", formationDTO);
        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

    /**
     * Update a formation.
     *
     * @param formationDTO the entity to save.
     * @return the persisted entity.
     */
    public FormationDTO update(FormationDTO formationDTO) {
        log.debug("Request to update Formation : {}", formationDTO);
        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

    /**
     * Partially update a formation.
     *
     * @param formationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormationDTO> partialUpdate(FormationDTO formationDTO) {
        log.debug("Request to partially update Formation : {}", formationDTO);

        return formationRepository
            .findById(formationDTO.getId())
            .map(existingFormation -> {
                formationMapper.partialUpdate(existingFormation, formationDTO);

                return existingFormation;
            })
            .map(formationRepository::save)
            .map(formationMapper::toDto);
    }

    /**
     * Get all the formations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FormationDTO> findAll() {
        log.debug("Request to get all Formations");
        return formationRepository.findAll().stream().map(formationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one formation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormationDTO> findOne(Long id) {
        log.debug("Request to get Formation : {}", id);
        return formationRepository.findById(id).map(formationMapper::toDto);
    }

    /**
     * Delete the formation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Formation : {}", id);
        formationRepository.deleteById(id);
    }
}
