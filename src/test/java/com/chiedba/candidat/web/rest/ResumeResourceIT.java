package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.repository.ResumeRepository;
import com.chiedba.candidat.service.dto.ResumeDTO;
import com.chiedba.candidat.service.mapper.ResumeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ResumeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResumeResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resumes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResumeMockMvc;

    private Resume resume;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume().description(DEFAULT_DESCRIPTION);
        return resume;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createUpdatedEntity(EntityManager em) {
        Resume resume = new Resume().description(UPDATED_DESCRIPTION);
        return resume;
    }

    @BeforeEach
    public void initTest() {
        resume = createEntity(em);
    }

    @Test
    @Transactional
    void createResume() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();
        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);
        restResumeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate + 1);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createResumeWithExistingId() throws Exception {
        // Create the Resume with an existing ID
        resume.setId(1L);
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResumeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setDescription(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        restResumeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList
        restResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc
            .perform(get(ENTITY_API_URL_ID, resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume
        Resume updatedResume = resumeRepository.findById(resume.getId()).get();
        // Disconnect from session so that the updates on updatedResume are not directly saved in db
        em.detach(updatedResume);
        updatedResume.description(UPDATED_DESCRIPTION);
        ResumeDTO resumeDTO = resumeMapper.toDto(updatedResume);

        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resumeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resumeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume.description(UPDATED_DESCRIPTION);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume.description(UPDATED_DESCRIPTION);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resumeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.toDto(resume);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resumeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeDelete = resumeRepository.findAll().size();

        // Delete the resume
        restResumeMockMvc
            .perform(delete(ENTITY_API_URL_ID, resume.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
