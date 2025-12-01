package com.api.quizAI.web.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ScoreBroadcastRequestDTO(
        @NotNull
        UUID scoreId,

        @NotNull
        Integer pointsEarned
) {
}
