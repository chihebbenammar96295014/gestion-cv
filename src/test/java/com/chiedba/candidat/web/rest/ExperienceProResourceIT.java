package com.chiedba.candidat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chiedba.candidat.IntegrationTest;
import com.chiedba.candidat.domain.ExperiencePro;
import com.chiedba.candidat.repository.ExperienceProRepository;
import com.chiedba.candidat.service.dto.ExperienceProDTO;
import com.chiedba.candidat.service.mapper.ExperienceProMapper;
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
 * Integration tests for the {@link ExperienceProResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExperienceProResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/experience-pros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExperienceProRepository experienceProRepository;

    @Autowired
    private ExperienceProMapper experienceProMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExperienceProMockMvc;

    private ExperiencePro experiencePro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExperiencePro createEntity(EntityManager em) {
        ExperiencePro experiencePro = new ExperiencePro()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .fonction(DEFAULT_FONCTION)
            .place(DEFAULT_PLACE);
        return experiencePro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExperiencePro createUpdatedEntity(EntityManager em) {
        ExperiencePro experiencePro = new ExperiencePro()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fonction(UPDATED_FONCTION)
            .place(UPDATED_PLACE);
        return experiencePro;
    }

    @BeforeEach
    public void initTest() {
        experiencePro = createEntity(em);
    }

    @Test
    @Transactional
    void createExperiencePro() throws Exception {
        int databaseSizeBeforeCreate = experienceProRepository.findAll().size();
        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);
        restExperienceProMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeCreate + 1);
        ExperiencePro testExperiencePro = experienceProList.get(experienceProList.size() - 1);
        assertThat(testExperiencePro.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExperiencePro.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testExperiencePro.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExperiencePro.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExperiencePro.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testExperiencePro.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    @Transactional
    void createExperienceProWithExistingId() throws Exception {
        // Create the ExperiencePro with an existing ID
        experiencePro.setId(1L);
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        int databaseSizeBeforeCreate = experienceProRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienceProMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceProRepository.findAll().size();
        // set the field null
        experiencePro.setTitle(null);

        // Create the ExperiencePro, which fails.
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        restExperienceProMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExperiencePros() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        // Get all the experienceProList
        restExperienceProMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiencePro.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    @Transactional
    void getExperiencePro() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        // Get the experiencePro
        restExperienceProMockMvc
            .perform(get(ENTITY_API_URL_ID, experiencePro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(experiencePro.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    @Transactional
    void getNonExistingExperiencePro() throws Exception {
        // Get the experiencePro
        restExperienceProMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExperiencePro() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();

        // Update the experiencePro
        ExperiencePro updatedExperiencePro = experienceProRepository.findById(experiencePro.getId()).get();
        // Disconnect from session so that the updates on updatedExperiencePro are not directly saved in db
        em.detach(updatedExperiencePro);
        updatedExperiencePro
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fonction(UPDATED_FONCTION)
            .place(UPDATED_PLACE);
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(updatedExperiencePro);

        restExperienceProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experienceProDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
        ExperiencePro testExperiencePro = experienceProList.get(experienceProList.size() - 1);
        assertThat(testExperiencePro.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExperiencePro.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExperiencePro.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExperiencePro.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExperiencePro.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testExperiencePro.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void putNonExistingExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experienceProDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExperienceProWithPatch() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();

        // Update the experiencePro using partial update
        ExperiencePro partialUpdatedExperiencePro = new ExperiencePro();
        partialUpdatedExperiencePro.setId(experiencePro.getId());

        partialUpdatedExperiencePro
            .startDate(UPDATED_START_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .place(UPDATED_PLACE);

        restExperienceProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperiencePro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperiencePro))
            )
            .andExpect(status().isOk());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
        ExperiencePro testExperiencePro = experienceProList.get(experienceProList.size() - 1);
        assertThat(testExperiencePro.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExperiencePro.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testExperiencePro.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExperiencePro.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExperiencePro.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testExperiencePro.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void fullUpdateExperienceProWithPatch() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();

        // Update the experiencePro using partial update
        ExperiencePro partialUpdatedExperiencePro = new ExperiencePro();
        partialUpdatedExperiencePro.setId(experiencePro.getId());

        partialUpdatedExperiencePro
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .fonction(UPDATED_FONCTION)
            .place(UPDATED_PLACE);

        restExperienceProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperiencePro.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperiencePro))
            )
            .andExpect(status().isOk());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
        ExperiencePro testExperiencePro = experienceProList.get(experienceProList.size() - 1);
        assertThat(testExperiencePro.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExperiencePro.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testExperiencePro.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExperiencePro.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExperiencePro.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testExperiencePro.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    @Transactional
    void patchNonExistingExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, experienceProDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExperiencePro() throws Exception {
        int databaseSizeBeforeUpdate = experienceProRepository.findAll().size();
        experiencePro.setId(count.incrementAndGet());

        // Create the ExperiencePro
        ExperienceProDTO experienceProDTO = experienceProMapper.toDto(experiencePro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienceProMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienceProDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExperiencePro in the database
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExperiencePro() throws Exception {
        // Initialize the database
        experienceProRepository.saveAndFlush(experiencePro);

        int databaseSizeBeforeDelete = experienceProRepository.findAll().size();

        // Delete the experiencePro
        restExperienceProMockMvc
            .perform(delete(ENTITY_API_URL_ID, experiencePro.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExperiencePro> experienceProList = experienceProRepository.findAll();
        assertThat(experienceProList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
