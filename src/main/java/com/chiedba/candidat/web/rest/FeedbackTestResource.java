package com.chiedba.candidat.web.rest;

import com.chiedba.candidat.repository.FeedbackTestRepository;
import com.chiedba.candidat.service.FeedbackTestService;
import com.chiedba.candidat.service.dto.FeedbackTestDTO;
import com.chiedba.candidat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.chiedba.candidat.domain.FeedbackTest}.
 */
@RestController
@RequestMapping("/api")
public class FeedbackTestResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackTestResource.class);

    private static final String ENTITY_NAME = "feedbackTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackTestService feedbackTestService;

    private final FeedbackTestRepository feedbackTestRepository;

    public FeedbackTestResource(FeedbackTestService feedbackTestService, FeedbackTestRepository feedbackTestRepository) {
        this.feedbackTestService = feedbackTestService;
        this.feedbackTestRepository = feedbackTestRepository;
    }

    /**
     * {@code POST  /feedback-tests} : Create a new feedbackTest.
     *
     * @param feedbackTestDTO the feedbackTestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackTestDTO, or with status {@code 400 (Bad Request)} if the feedbackTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-tests")
    public ResponseEntity<FeedbackTestDTO> createFeedbackTest(@RequestBody FeedbackTestDTO feedbackTestDTO) throws URISyntaxException {
        log.debug("REST request to save FeedbackTest : {}", feedbackTestDTO);
        if (feedbackTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new feedbackTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackTestDTO result = feedbackTestService.save(feedbackTestDTO);
        return ResponseEntity
            .created(new URI("/api/feedback-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feedback-tests/:id} : Updates an existing feedbackTest.
     *
     * @param id the id of the feedbackTestDTO to save.
     * @param feedbackTestDTO the feedbackTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackTestDTO,
     * or with status {@code 400 (Bad Request)} if the feedbackTestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-tests/{id}")
    public ResponseEntity<FeedbackTestDTO> updateFeedbackTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackTestDTO feedbackTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackTest : {}, {}", id, feedbackTestDTO);
        if (feedbackTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeedbackTestDTO result = feedbackTestService.update(feedbackTestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackTestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feedback-tests/:id} : Partial updates given fields of an existing feedbackTest, field will ignore if it is null
     *
     * @param id the id of the feedbackTestDTO to save.
     * @param feedbackTestDTO the feedbackTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackTestDTO,
     * or with status {@code 400 (Bad Request)} if the feedbackTestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackTestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackTestDTO> partialUpdateFeedbackTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackTestDTO feedbackTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackTest partially : {}, {}", id, feedbackTestDTO);
        if (feedbackTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackTestDTO> result = feedbackTestService.partialUpdate(feedbackTestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackTestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-tests} : get all the feedbackTests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackTests in body.
     */
    @GetMapping("/feedback-tests")
    public ResponseEntity<List<FeedbackTestDTO>> getAllFeedbackTests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FeedbackTests");
        Page<FeedbackTestDTO> page = feedbackTestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feedback-tests/:id} : get the "id" feedbackTest.
     *
     * @param id the id of the feedbackTestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackTestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-tests/{id}")
    public ResponseEntity<FeedbackTestDTO> getFeedbackTest(@PathVariable Long id) {
        log.debug("REST request to get FeedbackTest : {}", id);
        Optional<FeedbackTestDTO> feedbackTestDTO = feedbackTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackTestDTO);
    }

    /**
     * {@code DELETE  /feedback-tests/:id} : delete the "id" feedbackTest.
     *
     * @param id the id of the feedbackTestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-tests/{id}")
    public ResponseEntity<Void> deleteFeedbackTest(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackTest : {}", id);
        feedbackTestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
