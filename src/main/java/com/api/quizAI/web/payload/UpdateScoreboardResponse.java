package com.api.quizAI.web.payload;


import com.api.quizAI.core.domain.User;


public record UpdateScoreboardResponse(
        User player,
        Integer pointsEarned
) {
}
