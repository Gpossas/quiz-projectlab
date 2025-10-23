package com.api.quizAI.core.exceptions;

public class PlayerAlreadyInOtherRoom extends RuntimeException {
    public PlayerAlreadyInOtherRoom() {
        super("Jogador já está em outra sala");
    }
}
