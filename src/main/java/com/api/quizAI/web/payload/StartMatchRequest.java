package com.api.quizAI.web.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StartMatchRequest(
        @NotNull
        UUID roomId,

        @NotNull
        UUID playerId
) {
}
