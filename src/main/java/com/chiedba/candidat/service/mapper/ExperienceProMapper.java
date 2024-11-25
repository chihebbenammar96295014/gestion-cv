package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.ExperiencePro;
import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.service.dto.ExperienceProDTO;
import com.chiedba.candidat.service.dto.ResumeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExperiencePro} and its DTO {@link ExperienceProDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExperienceProMapper extends EntityMapper<ExperienceProDTO, ExperiencePro> {
    @Mapping(target = "resume", source = "resume", qualifiedByName = "resumeId")
    ExperienceProDTO toDto(ExperiencePro s);

    @Named("resumeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResumeDTO toDtoResumeId(Resume resume);
}
