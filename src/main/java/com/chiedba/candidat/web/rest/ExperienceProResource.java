package com.chiedba.candidat.web.rest;

import com.chiedba.candidat.repository.ExperienceProRepository;
import com.chiedba.candidat.service.ExperienceProService;
import com.chiedba.candidat.service.dto.ExperienceProDTO;
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
 * REST controller for managing {@link com.chiedba.candidat.domain.ExperiencePro}.
 */
@RestController
@RequestMapping("/api")
public class ExperienceProResource {

    private final Logger log = LoggerFactory.getLogger(ExperienceProResource.class);

    private static final String ENTITY_NAME = "experiencePro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExperienceProService experienceProService;

    private final ExperienceProRepository experienceProRepository;

    public ExperienceProResource(ExperienceProService experienceProService, ExperienceProRepository experienceProRepository) {
        this.experienceProService = experienceProService;
        this.experienceProRepository = experienceProRepository;
    }

    /**
     * {@code POST  /experience-pros} : Create a new experiencePro.
     *
     * @param experienceProDTO the experienceProDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new experienceProDTO, or with status {@code 400 (Bad Request)} if the experiencePro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experience-pros")
    public ResponseEntity<ExperienceProDTO> createExperiencePro(@Valid @RequestBody ExperienceProDTO experienceProDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExperiencePro : {}", experienceProDTO);
        if (experienceProDTO.getId() != null) {
            throw new BadRequestAlertException("A new experiencePro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperienceProDTO result = experienceProService.save(experienceProDTO);
        return ResponseEntity
            .created(new URI("/api/experience-pros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /experience-pros/:id} : Updates an existing experiencePro.
     *
     * @param id the id of the experienceProDTO to save.
     * @param experienceProDTO the experienceProDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experienceProDTO,
     * or with status {@code 400 (Bad Request)} if the experienceProDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the experienceProDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experience-pros/{id}")
    public ResponseEntity<ExperienceProDTO> updateExperiencePro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExperienceProDTO experienceProDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExperiencePro : {}, {}", id, experienceProDTO);
        if (experienceProDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experienceProDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienceProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExperienceProDTO result = experienceProService.update(experienceProDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, experienceProDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /experience-pros/:id} : Partial updates given fields of an existing experiencePro, field will ignore if it is null
     *
     * @param id the id of the experienceProDTO to save.
     * @param experienceProDTO the experienceProDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experienceProDTO,
     * or with status {@code 400 (Bad Request)} if the experienceProDTO is not valid,
     * or with status {@code 404 (Not Found)} if the experienceProDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the experienceProDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/experience-pros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExperienceProDTO> partialUpdateExperiencePro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExperienceProDTO experienceProDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExperiencePro partially : {}, {}", id, experienceProDTO);
        if (experienceProDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experienceProDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienceProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExperienceProDTO> result = experienceProService.partialUpdate(experienceProDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, experienceProDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /experience-pros} : get all the experiencePros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experiencePros in body.
     */
    @GetMapping("/experience-pros")
    public List<ExperienceProDTO> getAllExperiencePros() {
        log.debug("REST request to get all ExperiencePros");
        return experienceProService.findAll();
    }

    /**
     * {@code GET  /experience-pros/:id} : get the "id" experiencePro.
     *
     * @param id the id of the experienceProDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the experienceProDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experience-pros/{id}")
    public ResponseEntity<ExperienceProDTO> getExperiencePro(@PathVariable Long id) {
        log.debug("REST request to get ExperiencePro : {}", id);
        Optional<ExperienceProDTO> experienceProDTO = experienceProService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experienceProDTO);
    }

    /**
     * {@code DELETE  /experience-pros/:id} : delete the "id" experiencePro.
     *
     * @param id the id of the experienceProDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experience-pros/{id}")
    public ResponseEntity<Void> deleteExperiencePro(@PathVariable Long id) {
        log.debug("REST request to delete ExperiencePro : {}", id);
        experienceProService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
