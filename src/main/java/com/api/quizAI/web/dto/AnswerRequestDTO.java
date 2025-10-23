package com.api.quizAI.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AnswerRequestDTO(
        @NotNull
        UUID playerId,

        @NotNull
        UUID answerId,

        @NotNull
        UUID roomId,

        @NotNull
        UUID questionId,

        @NotNull
        UUID scoreId
) {
}
