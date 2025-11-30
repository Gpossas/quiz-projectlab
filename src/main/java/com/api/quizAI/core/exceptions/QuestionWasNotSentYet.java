package com.api.quizAI.core.exceptions;

public class QuestionWasNotSentYet extends RuntimeException {
    public QuestionWasNotSentYet() {
        super("Questão ainda não foi liberada pelo servidor");
    }
}
