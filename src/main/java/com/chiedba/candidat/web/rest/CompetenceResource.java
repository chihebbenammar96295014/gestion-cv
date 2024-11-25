package com.chiedba.candidat.web.rest;

import com.chiedba.candidat.repository.CompetenceRepository;
import com.chiedba.candidat.service.CompetenceService;
import com.chiedba.candidat.service.dto.CompetenceDTO;
import com.chiedba.candidat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chiedba.candidat.domain.Competence}.
 */
@RestController
@RequestMapping("/api")
public class CompetenceResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceResource.class);

    private static final String ENTITY_NAME = "competence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetenceService competenceService;

    private final CompetenceRepository competenceRepository;

    public CompetenceResource(CompetenceService competenceService, CompetenceRepository competenceRepository) {
        this.competenceService = competenceService;
        this.competenceRepository = competenceRepository;
    }

    /**
     * {@code POST  /competences} : Create a new competence.
     *
     * @param competenceDTO the competenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competenceDTO, or with status {@code 400 (Bad Request)} if the competence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competences")
    public ResponseEntity<CompetenceDTO> createCompetence(@Valid @RequestBody CompetenceDTO competenceDTO) throws URISyntaxException {
        log.debug("REST request to save Competence : {}", competenceDTO);
        if (competenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new competence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompetenceDTO result = competenceService.save(competenceDTO);
        return ResponseEntity
            .created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competences/:id} : Updates an existing competence.
     *
     * @param id the id of the competenceDTO to save.
     * @param competenceDTO the competenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competenceDTO,
     * or with status {@code 400 (Bad Request)} if the competenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competences/{id}")
    public ResponseEntity<CompetenceDTO> updateCompetence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompetenceDTO competenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Competence : {}, {}", id, competenceDTO);
        if (competenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompetenceDTO result = competenceService.update(competenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /competences/:id} : Partial updates given fields of an existing competence, field will ignore if it is null
     *
     * @param id the id of the competenceDTO to save.
     * @param competenceDTO the competenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competenceDTO,
     * or with status {@code 400 (Bad Request)} if the competenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the competenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the competenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/competences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompetenceDTO> partialUpdateCompetence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompetenceDTO competenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Competence partially : {}, {}", id, competenceDTO);
        if (competenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompetenceDTO> result = competenceService.partialUpdate(competenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /competences} : get all the competences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competences in body.
     */
    @GetMapping("/competences")
    public List<CompetenceDTO> getAllCompetences() {
        log.debug("REST request to get all Competences");
        return competenceService.findAll();
    }

    /**
     * {@code GET  /competences/:id} : get the "id" competence.
     *
     * @param id the id of the competenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competences/{id}")
    public ResponseEntity<CompetenceDTO> getCompetence(@PathVariable Long id) {
        log.debug("REST request to get Competence : {}", id);
        Optional<CompetenceDTO> competenceDTO = competenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competenceDTO);
    }

    /**
     * {@code DELETE  /competences/:id} : delete the "id" competence.
     *
     * @param id the id of the competenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competences/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        log.debug("REST request to delete Competence : {}", id);
        competenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
