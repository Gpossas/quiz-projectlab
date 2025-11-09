package com.api.quizAI.web.dto;


import com.api.quizAI.core.domain.User;

import java.util.UUID;

public record PlayerScoreDTO(
        UUID id,
        Integer score,
        User player
) {
}
