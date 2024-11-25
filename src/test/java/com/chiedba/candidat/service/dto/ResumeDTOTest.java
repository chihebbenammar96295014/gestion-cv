package com.chiedba.candidat.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.chiedba.candidat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResumeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResumeDTO.class);
        ResumeDTO resumeDTO1 = new ResumeDTO();
        resumeDTO1.setId(1L);
        ResumeDTO resumeDTO2 = new ResumeDTO();
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
        resumeDTO2.setId(resumeDTO1.getId());
        assertThat(resumeDTO1).isEqualTo(resumeDTO2);
        resumeDTO2.setId(2L);
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
        resumeDTO1.setId(null);
        assertThat(resumeDTO1).isNotEqualTo(resumeDTO2);
    }
}
