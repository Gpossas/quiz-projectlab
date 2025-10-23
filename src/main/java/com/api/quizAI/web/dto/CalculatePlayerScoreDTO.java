package com.api.quizAI.web.dto;



import java.time.OffsetDateTime;
import java.util.UUID;

public record CalculatePlayerScoreDTO(
        UUID scoreId,
        UUID answerId,
        int totalTimeToAnswerQuestionInSeconds,
        OffsetDateTime questionWasSentAt,
        boolean isCorrectAnswer
) {
}
