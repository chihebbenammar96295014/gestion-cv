package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.Formation;
import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.service.dto.FormationDTO;
import com.chiedba.candidat.service.dto.ResumeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {
    @Mapping(target = "resume", source = "resume", qualifiedByName = "resumeId")
    FormationDTO toDto(Formation s);

    @Named("resumeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResumeDTO toDtoResumeId(Resume resume);
}
