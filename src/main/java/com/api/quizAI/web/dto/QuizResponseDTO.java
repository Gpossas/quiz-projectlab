package com.api.quizAI.web.dto;

import com.api.quizAI.core.domain.Quiz;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record QuizResponseDTO(
        UUID id,
        String topic,
        Set<QuestionDTO> questions
) {
    public static QuizResponseDTO domainToDTO(Quiz quiz)
    {
        return new QuizResponseDTO(
                quiz.getId(),
                quiz.getTopic(),
                quiz.getQuestions().stream().map(
                        question -> new QuestionDTO(
                                question.getId(),
                                question.getValue(),
                                question.getAnswers().stream().map(
                                        answer -> new AnswerDTO(
                                                answer.getId(),
                                                answer.getValue())
                                ).collect(Collectors.toSet())
                        )
                ).collect(Collectors.toSet())
        );
    }
}

record AnswerDTO(
        UUID id,
        String value
){}