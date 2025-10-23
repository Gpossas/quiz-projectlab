package com.api.quizAI.business.factories.strategies;

public class IntermediateBonus implements ScoreBonus
{
    @Override
    public boolean wasAchieved(int percentage) {
        return percentage <= 50;
    }

    @Override
    public int getBonus() {
        return 5;
    }
}
