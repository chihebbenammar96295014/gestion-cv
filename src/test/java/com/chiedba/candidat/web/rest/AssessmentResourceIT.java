package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.Assessment;
import com.chiedba.candidat.repository.AssessmentRepository;
import com.chiedba.candidat.service.AssessmentService;
import com.chiedba.candidat.service.dto.AssessmentDTO;
import com.chiedba.candidat.service.mapper.AssessmentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssessmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssessmentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentRepository assessmentRepositoryMock;

    @Autowired
    private AssessmentMapper assessmentMapper;

    @Mock
    private AssessmentService assessmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssessmentMockMvc;

    private Assessment assessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createEntity(EntityManager em) {
        Assessment assessment = new Assessment()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .niveau(DEFAULT_NIVEAU)
            .date(DEFAULT_DATE);
        return assessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createUpdatedEntity(EntityManager em) {
        Assessment assessment = new Assessment()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .date(UPDATED_DATE);
        return assessment;
    }

    @BeforeEach
    public void initTest() {
        assessment = createEntity(em);
    }

    @Test
    @Transactional
    void createAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();
        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);
        restAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAssessment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssessment.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testAssessment.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createAssessmentWithExistingId() throws Exception {
        // Create the Assessment with an existing ID
        assessment.setId(1L);
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().size();
        // set the field null
        assessment.setNom(null);

        // Create the Assessment, which fails.
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        restAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssessments() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get all the assessmentList
        restAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssessmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assessmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssessmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assessmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssessmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assessmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssessmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assessmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get the assessment
        restAssessmentMockMvc
            .perform(get(ENTITY_API_URL_ID, assessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assessment.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssessment() throws Exception {
        // Get the assessment
        restAssessmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment
        Assessment updatedAssessment = assessmentRepository.findById(assessment.getId()).get();
        // Disconnect from session so that the updates on updatedAssessment are not directly saved in db
        em.detach(updatedAssessment);
        updatedAssessment.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).niveau(UPDATED_NIVEAU).date(UPDATED_DATE);
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(updatedAssessment);

        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssessment.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAssessment.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).niveau(UPDATED_NIVEAU);

        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssessment.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAssessment.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).niveau(UPDATED_NIVEAU).date(UPDATED_DATE);

        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            )
            .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssessment.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAssessment.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assessmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();
        assessment.setId(count.incrementAndGet());

        // Create the Assessment
        AssessmentDTO assessmentDTO = assessmentMapper.toDto(assessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        int databaseSizeBeforeDelete = assessmentRepository.findAll().size();

        // Delete the assessment
        restAssessmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, assessment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
