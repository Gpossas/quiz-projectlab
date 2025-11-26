package com.api.quizAI.business.factories;

import com.api.quizAI.business.factories.strategies.IntermediateBonus;
import com.api.quizAI.business.factories.strategies.NoobBonus;
import com.api.quizAI.business.factories.strategies.ScoreBonus;
import com.api.quizAI.business.factories.strategies.SuperFastBonus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoreFactory
{
    private final List<ScoreBonus> scoreBonuses = List.of(
            new SuperFastBonus(),
            new IntermediateBonus(),
            new NoobBonus());

    public int getBonus(OffsetDateTime timeQuestionWasSent, int totalTimeInSeconds)
    {
        long secondsTookToAnswer = Duration.between(timeQuestionWasSent, OffsetDateTime.now()).getSeconds();
        int percentage = (int) (secondsTookToAnswer * 100.0) / totalTimeInSeconds;

        for (ScoreBonus scoreBonus: scoreBonuses)
        {
            if (scoreBonus.wasAchieved(percentage))
            {
                return scoreBonus.getBonus();
            }
        }

        return 0;
    }
}
