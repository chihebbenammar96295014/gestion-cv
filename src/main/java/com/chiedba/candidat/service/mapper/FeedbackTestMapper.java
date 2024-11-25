package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.AppUser;
import com.chiedba.candidat.domain.Assessment;
import com.chiedba.candidat.domain.FeedbackTest;
import com.chiedba.candidat.service.dto.AppUserDTO;
import com.chiedba.candidat.service.dto.AssessmentDTO;
import com.chiedba.candidat.service.dto.FeedbackTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FeedbackTest} and its DTO {@link FeedbackTestDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackTestMapper extends EntityMapper<FeedbackTestDTO, FeedbackTest> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserId")
    @Mapping(target = "assessment", source = "assessment", qualifiedByName = "assessmentId")
    FeedbackTestDTO toDto(FeedbackTest s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("assessmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssessmentDTO toDtoAssessmentId(Assessment assessment);
}
