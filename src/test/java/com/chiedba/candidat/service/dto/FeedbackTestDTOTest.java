package com.chiedba.candidat.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.chiedba.candidat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackTestDTO.class);
        FeedbackTestDTO feedbackTestDTO1 = new FeedbackTestDTO();
        feedbackTestDTO1.setId(1L);
        FeedbackTestDTO feedbackTestDTO2 = new FeedbackTestDTO();
        assertThat(feedbackTestDTO1).isNotEqualTo(feedbackTestDTO2);
        feedbackTestDTO2.setId(feedbackTestDTO1.getId());
        assertThat(feedbackTestDTO1).isEqualTo(feedbackTestDTO2);
        feedbackTestDTO2.setId(2L);
        assertThat(feedbackTestDTO1).isNotEqualTo(feedbackTestDTO2);
        feedbackTestDTO1.setId(null);
        assertThat(feedbackTestDTO1).isNotEqualTo(feedbackTestDTO2);
    }
}
