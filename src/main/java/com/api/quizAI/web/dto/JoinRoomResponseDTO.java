package com.api.quizAI.web.dto;

import com.api.quizAI.core.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record JoinRoomResponseDTO(
        UUID roomId,

        String roomCode,

        Boolean isPublic,

        @Schema(example = "8")
        Integer maxNumberOfPlayers,

        User owner,

        PlayerScoreDTO scoreboard
) {
}
