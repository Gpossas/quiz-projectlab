package com.api.quizAI.business.factories.strategies;


public interface ScoreBonus
{
    boolean wasAchieved(int percentage);
    int getBonus();
}
