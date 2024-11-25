package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.Competence;
import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.service.dto.CompetenceDTO;
import com.chiedba.candidat.service.dto.ResumeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Competence} and its DTO {@link CompetenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetenceMapper extends EntityMapper<CompetenceDTO, Competence> {
    @Mapping(target = "resume", source = "resume", qualifiedByName = "resumeId")
    CompetenceDTO toDto(Competence s);

    @Named("resumeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResumeDTO toDtoResumeId(Resume resume);
}
