package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.service.dto.ResumeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resume} and its DTO {@link ResumeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResumeMapper extends EntityMapper<ResumeDTO, Resume> {}
