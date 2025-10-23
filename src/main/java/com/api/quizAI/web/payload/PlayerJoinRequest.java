package com.api.quizAI.web.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlayerJoinRequest(
        @NotNull
        UUID scoreId
) {
}
