package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssessmentMapperTest {

    private AssessmentMapper assessmentMapper;

    @BeforeEach
    public void setUp() {
        assessmentMapper = new AssessmentMapperImpl();
    }
}
