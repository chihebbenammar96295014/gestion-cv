package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompetenceMapperTest {

    private CompetenceMapper competenceMapper;

    @BeforeEach
    public void setUp() {
        competenceMapper = new CompetenceMapperImpl();
    }
}
