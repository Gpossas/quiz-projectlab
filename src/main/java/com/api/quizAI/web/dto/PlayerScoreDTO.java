package com.api.quizAI.web.dto;


import java.util.UUID;

public record PlayerScoreDTO(
        UUID id,
        Integer score
) {
}
