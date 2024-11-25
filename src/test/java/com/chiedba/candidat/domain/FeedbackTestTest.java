package com.chiedba.candidat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chiedba.candidat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackTest.class);
        FeedbackTest feedbackTest1 = new FeedbackTest();
        feedbackTest1.setId(1L);
        FeedbackTest feedbackTest2 = new FeedbackTest();
        feedbackTest2.setId(feedbackTest1.getId());
        assertThat(feedbackTest1).isEqualTo(feedbackTest2);
        feedbackTest2.setId(2L);
        assertThat(feedbackTest1).isNotEqualTo(feedbackTest2);
        feedbackTest1.setId(null);
        assertThat(feedbackTest1).isNotEqualTo(feedbackTest2);
    }
}
