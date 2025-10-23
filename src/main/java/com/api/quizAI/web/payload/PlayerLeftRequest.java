package com.api.quizAI.web.payload;

import java.util.UUID;

public record PlayerLeftRequest(
        UUID scoreId
) {
}
