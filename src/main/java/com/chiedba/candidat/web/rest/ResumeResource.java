package com.chiedba.candidat.web.rest;

import com.chiedba.candidat.repository.ResumeRepository;
import com.chiedba.candidat.service.ResumeService;
import com.chiedba.candidat.service.dto.ResumeDTO;
import com.chiedba.candidat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chiedba.candidat.domain.Resume}.
 */
@RestController
@RequestMapping("/api")
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResumeService resumeService;

    private final ResumeRepository resumeRepository;

    public ResumeResource(ResumeService resumeService, ResumeRepository resumeRepository) {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
    }

    /**
     * {@code POST  /resumes} : Create a new resume.
     *
     * @param resumeDTO the resumeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resumeDTO, or with status {@code 400 (Bad Request)} if the resume has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resumes")
    public ResponseEntity<ResumeDTO> createResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resumeDTO);
        if (resumeDTO.getId() != null) {
            throw new BadRequestAlertException("A new resume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResumeDTO result = resumeService.save(resumeDTO);
        return ResponseEntity
            .created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resumes/:id} : Updates an existing resume.
     *
     * @param id the id of the resumeDTO to save.
     * @param resumeDTO the resumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resumeDTO,
     * or with status {@code 400 (Bad Request)} if the resumeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resumes/{id}")
    public ResponseEntity<ResumeDTO> updateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResumeDTO resumeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resume : {}, {}", id, resumeDTO);
        if (resumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResumeDTO result = resumeService.update(resumeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resumeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resumes/:id} : Partial updates given fields of an existing resume, field will ignore if it is null
     *
     * @param id the id of the resumeDTO to save.
     * @param resumeDTO the resumeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resumeDTO,
     * or with status {@code 400 (Bad Request)} if the resumeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resumeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resumeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resumes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResumeDTO> partialUpdateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResumeDTO resumeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resume partially : {}, {}", id, resumeDTO);
        if (resumeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resumeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResumeDTO> result = resumeService.partialUpdate(resumeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resumeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resumes} : get all the resumes.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resumes in body.
     */
    @GetMapping("/resumes")
    public ResponseEntity<List<ResumeDTO>> getAllResumes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("appuser-is-null".equals(filter)) {
            log.debug("REST request to get all Resumes where appUser is null");
            return new ResponseEntity<>(resumeService.findAllWhereAppUserIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Resumes");
        Page<ResumeDTO> page = resumeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resumes/:id} : get the "id" resume.
     *
     * @param id the id of the resumeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resumeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResumeDTO> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        Optional<ResumeDTO> resumeDTO = resumeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resumeDTO);
    }

    /**
     * {@code DELETE  /resumes/:id} : delete the "id" resume.
     *
     * @param id the id of the resumeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
