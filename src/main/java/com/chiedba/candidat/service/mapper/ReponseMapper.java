package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.FeedbackTest;
import com.chiedba.candidat.domain.Reponse;
import com.chiedba.candidat.service.dto.FeedbackTestDTO;
import com.chiedba.candidat.service.dto.ReponseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reponse} and its DTO {@link ReponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReponseMapper extends EntityMapper<ReponseDTO, Reponse> {
    @Mapping(target = "feedbackTest", source = "feedbackTest", qualifiedByName = "feedbackTestId")
    ReponseDTO toDto(Reponse s);

    @Named("feedbackTestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FeedbackTestDTO toDtoFeedbackTestId(FeedbackTest feedbackTest);
}
