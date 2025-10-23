package com.api.quizAI.core.exceptions;

public class MatchCanNotStartWithoutQuiz extends RuntimeException {
    public MatchCanNotStartWithoutQuiz() {
        super("Não pode iniciar uma partida sem quiz");
    }
}
