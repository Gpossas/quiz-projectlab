package com.api.quizAI.web.payload;


import com.api.quizAI.core.domain.User;

import java.util.UUID;


public record ScoreboardResponse(
        UUID scoreId,
        User player,
        Integer pointsEarned
) {
}
