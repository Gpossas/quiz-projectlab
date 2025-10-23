package com.api.quizAI.web.payload;

import com.api.quizAI.core.domain.User;

import java.util.UUID;

public record UserScoreboardResponse(
        UUID id,
        int score,
        User player
) {
}
