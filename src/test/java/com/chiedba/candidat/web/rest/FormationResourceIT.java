package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.Formation;
import com.chiedba.candidat.repository.FormationRepository;
import com.chiedba.candidat.service.dto.FormationDTO;
import com.chiedba.candidat.service.mapper.FormationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .place(DEFAULT_PLACE);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFormation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFormation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormation.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setTitle(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).get();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFormation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFormation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormation.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation.place(UPDATED_PLACE);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFormation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFormation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormation.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFormation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFormation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormation.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
