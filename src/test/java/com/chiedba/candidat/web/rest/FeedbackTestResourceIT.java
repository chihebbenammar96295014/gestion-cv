package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.FeedbackTest;
import com.chiedba.candidat.repository.FeedbackTestRepository;
import com.chiedba.candidat.service.dto.FeedbackTestDTO;
import com.chiedba.candidat.service.mapper.FeedbackTestMapper;
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
 * Integration tests for the {@link FeedbackTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackTestResourceIT {

    private static final Float DEFAULT_NOTE = 1F;
    private static final Float UPDATED_NOTE = 2F;

    private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedback-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackTestRepository feedbackTestRepository;

    @Autowired
    private FeedbackTestMapper feedbackTestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackTestMockMvc;

    private FeedbackTest feedbackTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackTest createEntity(EntityManager em) {
        FeedbackTest feedbackTest = new FeedbackTest().note(DEFAULT_NOTE).commentaires(DEFAULT_COMMENTAIRES);
        return feedbackTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackTest createUpdatedEntity(EntityManager em) {
        FeedbackTest feedbackTest = new FeedbackTest().note(UPDATED_NOTE).commentaires(UPDATED_COMMENTAIRES);
        return feedbackTest;
    }

    @BeforeEach
    public void initTest() {
        feedbackTest = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedbackTest() throws Exception {
        int databaseSizeBeforeCreate = feedbackTestRepository.findAll().size();
        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);
        restFeedbackTestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackTest testFeedbackTest = feedbackTestList.get(feedbackTestList.size() - 1);
        assertThat(testFeedbackTest.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFeedbackTest.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
    }

    @Test
    @Transactional
    void createFeedbackTestWithExistingId() throws Exception {
        // Create the FeedbackTest with an existing ID
        feedbackTest.setId(1L);
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        int databaseSizeBeforeCreate = feedbackTestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackTestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackTests() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        // Get all the feedbackTestList
        restFeedbackTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }

    @Test
    @Transactional
    void getFeedbackTest() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        // Get the feedbackTest
        restFeedbackTestMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackTest.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackTest() throws Exception {
        // Get the feedbackTest
        restFeedbackTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackTest() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();

        // Update the feedbackTest
        FeedbackTest updatedFeedbackTest = feedbackTestRepository.findById(feedbackTest.getId()).get();
        // Disconnect from session so that the updates on updatedFeedbackTest are not directly saved in db
        em.detach(updatedFeedbackTest);
        updatedFeedbackTest.note(UPDATED_NOTE).commentaires(UPDATED_COMMENTAIRES);
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(updatedFeedbackTest);

        restFeedbackTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackTestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackTest testFeedbackTest = feedbackTestList.get(feedbackTestList.size() - 1);
        assertThat(testFeedbackTest.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFeedbackTest.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackTestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackTestWithPatch() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();

        // Update the feedbackTest using partial update
        FeedbackTest partialUpdatedFeedbackTest = new FeedbackTest();
        partialUpdatedFeedbackTest.setId(feedbackTest.getId());

        partialUpdatedFeedbackTest.commentaires(UPDATED_COMMENTAIRES);

        restFeedbackTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackTest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackTest))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackTest testFeedbackTest = feedbackTestList.get(feedbackTestList.size() - 1);
        assertThat(testFeedbackTest.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFeedbackTest.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackTestWithPatch() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();

        // Update the feedbackTest using partial update
        FeedbackTest partialUpdatedFeedbackTest = new FeedbackTest();
        partialUpdatedFeedbackTest.setId(feedbackTest.getId());

        partialUpdatedFeedbackTest.note(UPDATED_NOTE).commentaires(UPDATED_COMMENTAIRES);

        restFeedbackTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackTest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackTest))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackTest testFeedbackTest = feedbackTestList.get(feedbackTestList.size() - 1);
        assertThat(testFeedbackTest.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFeedbackTest.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackTestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackTest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackTestRepository.findAll().size();
        feedbackTest.setId(count.incrementAndGet());

        // Create the FeedbackTest
        FeedbackTestDTO feedbackTestDTO = feedbackTestMapper.toDto(feedbackTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackTestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackTest in the database
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackTest() throws Exception {
        // Initialize the database
        feedbackTestRepository.saveAndFlush(feedbackTest);

        int databaseSizeBeforeDelete = feedbackTestRepository.findAll().size();

        // Delete the feedbackTest
        restFeedbackTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackTest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeedbackTest> feedbackTestList = feedbackTestRepository.findAll();
        assertThat(feedbackTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
