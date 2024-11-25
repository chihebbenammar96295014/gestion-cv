package com.chiedba.candidat.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.chiedba.candidat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienceProDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienceProDTO.class);
        ExperienceProDTO experienceProDTO1 = new ExperienceProDTO();
        experienceProDTO1.setId(1L);
        ExperienceProDTO experienceProDTO2 = new ExperienceProDTO();
        assertThat(experienceProDTO1).isNotEqualTo(experienceProDTO2);
        experienceProDTO2.setId(experienceProDTO1.getId());
        assertThat(experienceProDTO1).isEqualTo(experienceProDTO2);
        experienceProDTO2.setId(2L);
        assertThat(experienceProDTO1).isNotEqualTo(experienceProDTO2);
        experienceProDTO1.setId(null);
        assertThat(experienceProDTO1).isNotEqualTo(experienceProDTO2);
    }
}
