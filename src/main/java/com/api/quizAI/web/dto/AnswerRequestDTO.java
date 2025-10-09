package com.api.quizAI.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AnswerRequestDTO(
        @NotNull
        UUID userId,

        @NotNull
        UUID answerId,

        @NotNull
        UUID roomId
) {
}
