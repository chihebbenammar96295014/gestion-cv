package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExperienceProMapperTest {

    private ExperienceProMapper experienceProMapper;

    @BeforeEach
    public void setUp() {
        experienceProMapper = new ExperienceProMapperImpl();
    }
}
