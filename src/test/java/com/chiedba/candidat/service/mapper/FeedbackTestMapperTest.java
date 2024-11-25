package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackTestMapperTest {

    private FeedbackTestMapper feedbackTestMapper;

    @BeforeEach
    public void setUp() {
        feedbackTestMapper = new FeedbackTestMapperImpl();
    }
}
