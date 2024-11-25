package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumeMapperTest {

    private ResumeMapper resumeMapper;

    @BeforeEach
    public void setUp() {
        resumeMapper = new ResumeMapperImpl();
    }
}
