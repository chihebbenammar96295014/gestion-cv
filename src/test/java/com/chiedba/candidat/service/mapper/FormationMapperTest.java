package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormationMapperTest {

    private FormationMapper formationMapper;

    @BeforeEach
    public void setUp() {
        formationMapper = new FormationMapperImpl();
    }
}
