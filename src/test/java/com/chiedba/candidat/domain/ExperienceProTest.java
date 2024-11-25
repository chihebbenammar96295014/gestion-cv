package com.chiedba.candidat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chiedba.candidat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienceProTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperiencePro.class);
        ExperiencePro experiencePro1 = new ExperiencePro();
        experiencePro1.setId(1L);
        ExperiencePro experiencePro2 = new ExperiencePro();
        experiencePro2.setId(experiencePro1.getId());
        assertThat(experiencePro1).isEqualTo(experiencePro2);
        experiencePro2.setId(2L);
        assertThat(experiencePro1).isNotEqualTo(experiencePro2);
        experiencePro1.setId(null);
        assertThat(experiencePro1).isNotEqualTo(experiencePro2);
    }
}
