package com.api.quizAI.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ScoreBroadcastRequestDTO(
        @NotNull
        UUID scoreId,

        @Min(0)
        @NotNull
        Integer pointsEarned
) {
}
