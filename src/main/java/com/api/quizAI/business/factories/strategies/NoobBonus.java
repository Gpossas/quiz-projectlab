package com.api.quizAI.business.factories.strategies;

public class NoobBonus implements ScoreBonus
{
    @Override
    public boolean wasAchieved(int percentage) {
        return percentage <= 70;
    }

    @Override
    public int getBonus() {
        return 3;
    }
}
