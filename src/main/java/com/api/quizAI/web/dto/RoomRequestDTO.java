package com.api.quizAI.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoomRequestDTO(
        @NotNull
        boolean isPublic,

        @Schema(example = "8")
        int maxNumberOfPlayersInRoom,

        @Max(value = 60, message = "O tempo máximo entre questões é 60 segundos")
        @Min(value = 5, message = "O tempo mínimo entre questões é 5 segundos")
        @NotNull
        int waitTimeInSeconds,

        @NotNull
        UUID ownerId
) {
}
