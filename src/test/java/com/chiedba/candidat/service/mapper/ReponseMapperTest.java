package com.chiedba.candidat.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReponseMapperTest {

    private ReponseMapper reponseMapper;

    @BeforeEach
    public void setUp() {
        reponseMapper = new ReponseMapperImpl();
    }
}