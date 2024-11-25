package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.Assessment;
import com.chiedba.candidat.domain.Question;
import com.chiedba.candidat.service.dto.AssessmentDTO;
import com.chiedba.candidat.service.dto.QuestionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assessment} and its DTO {@link AssessmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssessmentMapper extends EntityMapper<AssessmentDTO, Assessment> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "questionIdSet")
    AssessmentDTO toDto(Assessment s);

    @Mapping(target = "removeQuestion", ignore = true)
    Assessment toEntity(AssessmentDTO assessmentDTO);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("questionIdSet")
    default Set<QuestionDTO> toDtoQuestionIdSet(Set<Question> question) {
        return question.stream().map(this::toDtoQuestionId).collect(Collectors.toSet());
    }
}
