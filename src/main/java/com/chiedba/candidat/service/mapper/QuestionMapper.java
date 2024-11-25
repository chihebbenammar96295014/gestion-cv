package com.chiedba.candidat.service.mapper;

import com.chiedba.candidat.domain.Question;
import com.chiedba.candidat.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {}
