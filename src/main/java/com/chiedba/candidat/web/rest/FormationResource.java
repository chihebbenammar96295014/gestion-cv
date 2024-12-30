package com.chiedba.candidat.web.rest;

import com.chiedba.candidat.repository.FormationRepository;
import com.chiedba.candidat.service.FormationService;
import com.chiedba.candidat.service.dto.FormationDTO;
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
 * REST controller for managing {@link com.chiedba.candidat.domain.Formation}.
 */
@RestController
@RequestMapping("/api")
public class FormationResource {

    private final Logger log = LoggerFactory.getLogger(FormationResource.class);

    private static final String ENTITY_NAME = "formation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationService formationService;

    private final FormationRepository formationRepository;

    public FormationResource(FormationService formationService, FormationRepository formationRepository) {
        this.formationService = formationService;
        this.formationRepository = formationRepository;
    }

    /**
     * {@code POST  /formations} : Create a new formation.
     *
     * @param formationDTO the formationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationDTO, or with status {@code 400 (Bad Request)} if the formation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formations")
    public ResponseEntity<FormationDTO> createFormation(@Valid @RequestBody FormationDTO formationDTO) throws URISyntaxException {
        log.debug("REST request to save Formation : {}", formationDTO);
        if (formationDTO.getId() != null) {
            throw new BadRequestAlertException("A new formation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationDTO result = formationService.save(formationDTO);
        return ResponseEntity
            .created(new URI("/api/formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formations/:id} : Updates an existing formation.
     *
     * @param id the id of the formationDTO to save.
     * @param formationDTO the formationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationDTO,
     * or with status {@code 400 (Bad Request)} if the formationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formations/{id}")
    public ResponseEntity<FormationDTO> updateFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormationDTO formationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Formation : {}, {}", id, formationDTO);
        if (formationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormationDTO result = formationService.update(formationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formations/:id} : Partial updates given fields of an existing formation, field will ignore if it is null
     *
     * @param id the id of the formationDTO to save.
     * @param formationDTO the formationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationDTO,
     * or with status {@code 400 (Bad Request)} if the formationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormationDTO> partialUpdateFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormationDTO formationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formation partially : {}, {}", id, formationDTO);
        if (formationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormationDTO> result = formationService.partialUpdate(formationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formations} : get all the formations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formations in body.
     */
    @GetMapping("/formations")
    public List<FormationDTO> getAllFormations() {
        log.debug("REST request to get all Formations");
        return formationService.findAll();
    }

    /**
     * {@code GET  /formations/:id} : get the "id" formation.
     *
     * @param id the id of the formationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formations/{id}")
    public ResponseEntity<FormationDTO> getFormation(@PathVariable Long id) {
        log.debug("REST request to get Formation : {}", id);
        Optional<FormationDTO> formationDTO = formationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationDTO);
    }

    /**
     * {@code DELETE  /formations/:id} : delete the "id" formation.
     *
     * @param id the id of the formationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formations/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        log.debug("REST request to delete Formation : {}", id);
        formationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
