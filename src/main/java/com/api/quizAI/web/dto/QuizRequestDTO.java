package com.api.quizAI.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record QuizRequestDTO(
        @Schema(example = "atletas olímpicos")
        @Length(max = 120, message = "O tópico deve ter até 120 caracteres")
        @NotBlank(message = "O tópico para as questões não deve estar em branco")
        String topic,

        @Schema(example = "8")
        @NotNull(message = "O número de questões deve ser indicado")
        @Positive(message = "O número de perguntas deve ser positivo")
        @Max(value = 15, message = "O número máximo de perguntas deve ser 15")
        Integer numberOfQuestions,

        @Schema(example = "4")
        @NotNull(message = "O número de alternativas deve ser indicado")
        @Positive(message = "O número de alternativas deve ser positivo")
        @Max(value = 6, message = "O número máximo de alternativas deve ser 6")
        Integer numberOfAnswers
) {}
