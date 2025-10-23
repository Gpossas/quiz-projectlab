package com.api.quizAI.business.factories.strategies;

public class SuperFastBonus implements ScoreBonus
{
    @Override
    public boolean wasAchieved(int percentage) {
        return percentage <= 33;
    }

    @Override
    public int getBonus()
    {
        return 7;
    }
}
