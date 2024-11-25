package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.Competence;
import com.chiedba.candidat.repository.CompetenceRepository;
import com.chiedba.candidat.service.dto.CompetenceDTO;
import com.chiedba.candidat.service.mapper.CompetenceMapper;
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
 * Integration tests for the {@link CompetenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompetenceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_NIVEAU = 1F;
    private static final Float UPDATED_NIVEAU = 2F;

    private static final String ENTITY_API_URL = "/api/competences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompetenceRepository competenceRepository;

    @Autowired
    private CompetenceMapper competenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetenceMockMvc;

    private Competence competence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competence createEntity(EntityManager em) {
        Competence competence = new Competence().name(DEFAULT_NAME).niveau(DEFAULT_NIVEAU);
        return competence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competence createUpdatedEntity(EntityManager em) {
        Competence competence = new Competence().name(UPDATED_NAME).niveau(UPDATED_NIVEAU);
        return competence;
    }

    @BeforeEach
    public void initTest() {
        competence = createEntity(em);
    }

    @Test
    @Transactional
    void createCompetence() throws Exception {
        int databaseSizeBeforeCreate = competenceRepository.findAll().size();
        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);
        restCompetenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeCreate + 1);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetence.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
    }

    @Test
    @Transactional
    void createCompetenceWithExistingId() throws Exception {
        // Create the Competence with an existing ID
        competence.setId(1L);
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        int databaseSizeBeforeCreate = competenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setName(null);

        // Create the Competence, which fails.
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        restCompetenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setNiveau(null);

        // Create the Competence, which fails.
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        restCompetenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompetences() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList
        restCompetenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.doubleValue())));
    }

    @Test
    @Transactional
    void getCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get the competence
        restCompetenceMockMvc
            .perform(get(ENTITY_API_URL_ID, competence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competence.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompetence() throws Exception {
        // Get the competence
        restCompetenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence
        Competence updatedCompetence = competenceRepository.findById(competence.getId()).get();
        // Disconnect from session so that the updates on updatedCompetence are not directly saved in db
        em.detach(updatedCompetence);
        updatedCompetence.name(UPDATED_NAME).niveau(UPDATED_NIVEAU);
        CompetenceDTO competenceDTO = competenceMapper.toDto(updatedCompetence);

        restCompetenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetence.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void putNonExistingCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetenceWithPatch() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence using partial update
        Competence partialUpdatedCompetence = new Competence();
        partialUpdatedCompetence.setId(competence.getId());

        partialUpdatedCompetence.niveau(UPDATED_NIVEAU);

        restCompetenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetence.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetence))
            )
            .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetence.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void fullUpdateCompetenceWithPatch() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence using partial update
        Competence partialUpdatedCompetence = new Competence();
        partialUpdatedCompetence.setId(competence.getId());

        partialUpdatedCompetence.name(UPDATED_NAME).niveau(UPDATED_NIVEAU);

        restCompetenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetence.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetence))
            )
            .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetence.getNiveau()).isEqualTo(UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void patchNonExistingCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competenceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();
        competence.setId(count.incrementAndGet());

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeDelete = competenceRepository.findAll().size();

        // Delete the competence
        restCompetenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, competence.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
