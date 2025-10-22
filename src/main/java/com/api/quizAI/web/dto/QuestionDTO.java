package com.api.quizAI.web.dto;

import com.api.quizAI.core.domain.Question;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record QuestionDTO(
        UUID id,
        String value,
        Set<AnswerDTO> answers
){
    public static QuestionDTO domainToDTO(Question question)
    {
        return new QuestionDTO(
                question.getId(),
                question.getValue(),
                question.getAnswers().stream().map(
                        answer -> new AnswerDTO(
                                answer.getId(),
                                answer.getValue())
                ).collect(Collectors.toSet())
        );
    }
}
