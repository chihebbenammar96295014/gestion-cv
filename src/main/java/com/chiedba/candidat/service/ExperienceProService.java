package com.chiedba.candidat.service;

import com.chiedba.candidat.domain.ExperiencePro;
import com.chiedba.candidat.repository.ExperienceProRepository;
import com.chiedba.candidat.service.dto.ExperienceProDTO;
import com.chiedba.candidat.service.mapper.ExperienceProMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExperiencePro}.
 */
@Service
@Transactional
public class ExperienceProService {

    private final Logger log = LoggerFactory.getLogger(ExperienceProService.class);

    private final ExperienceProRepository experienceProRepository;

    private final ExperienceProMapper experienceProMapper;

    public ExperienceProService(ExperienceProRepository experienceProRepository, ExperienceProMapper experienceProMapper) {
        this.experienceProRepository = experienceProRepository;
        this.experienceProMapper = experienceProMapper;
    }

    /**
     * Save a experiencePro.
     *
     * @param experienceProDTO the entity to save.
     * @return the persisted entity.
     */
    public ExperienceProDTO save(ExperienceProDTO experienceProDTO) {
        log.debug("Request to save ExperiencePro : {}", experienceProDTO);
        ExperiencePro experiencePro = experienceProMapper.toEntity(experienceProDTO);
        experiencePro = experienceProRepository.save(experiencePro);
        return experienceProMapper.toDto(experiencePro);
    }

    /**
     * Update a experiencePro.
     *
     * @param experienceProDTO the entity to save.
     * @return the persisted entity.
     */
    public ExperienceProDTO update(ExperienceProDTO experienceProDTO) {
        log.debug("Request to update ExperiencePro : {}", experienceProDTO);
        ExperiencePro experiencePro = experienceProMapper.toEntity(experienceProDTO);
        experiencePro = experienceProRepository.save(experiencePro);
        return experienceProMapper.toDto(experiencePro);
    }

    /**
     * Partially update a experiencePro.
     *
     * @param experienceProDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExperienceProDTO> partialUpdate(ExperienceProDTO experienceProDTO) {
        log.debug("Request to partially update ExperiencePro : {}", experienceProDTO);

        return experienceProRepository
            .findById(experienceProDTO.getId())
            .map(existingExperiencePro -> {
                experienceProMapper.partialUpdate(existingExperiencePro, experienceProDTO);

                return existingExperiencePro;
            })
            .map(experienceProRepository::save)
            .map(experienceProMapper::toDto);
    }

    /**
     * Get all the experiencePros.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExperienceProDTO> findAll() {
        log.debug("Request to get all ExperiencePros");
        return experienceProRepository.findAll().stream().map(experienceProMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one experiencePro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExperienceProDTO> findOne(Long id) {
        log.debug("Request to get ExperiencePro : {}", id);
        return experienceProRepository.findById(id).map(experienceProMapper::toDto);
    }

    /**
     * Delete the experiencePro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExperiencePro : {}", id);
        experienceProRepository.deleteById(id);
    }
}
