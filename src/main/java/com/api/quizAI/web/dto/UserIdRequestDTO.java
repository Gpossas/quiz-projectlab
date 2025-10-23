package com.api.quizAI.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserIdRequestDTO(
        @NotNull
        UUID userId
) {
}
