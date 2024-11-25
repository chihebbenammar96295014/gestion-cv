package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.AppUser;
import com.chiedba.candidat.domain.Resume;
import com.chiedba.candidat.service.dto.AppUserDTO;
import com.chiedba.candidat.service.dto.ResumeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "resume", source = "resume", qualifiedByName = "resumeId")
    AppUserDTO toDto(AppUser s);

    @Named("resumeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResumeDTO toDtoResumeId(Resume resume);
}
