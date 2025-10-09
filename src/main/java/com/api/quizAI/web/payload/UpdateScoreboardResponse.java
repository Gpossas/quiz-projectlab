package com.api.quizAI.web.payload;


import java.util.UUID;

public record UpdateScoreboardResponse(
        UUID userId,
        Integer pointsEarned
) {
}
