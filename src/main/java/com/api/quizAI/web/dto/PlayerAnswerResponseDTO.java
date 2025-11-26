package com.api.quizAI.web.dto;

import java.util.UUID;

public record PlayerAnswerResponseDTO(
        Integer pointsEarned,
        UUID correctAnswerId,
        UUID answerChosenId
) {
}
