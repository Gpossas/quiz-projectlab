package com.api.quizAI.core.exceptions;

public class RoomIsFull extends RuntimeException {
    public RoomIsFull() {
        super("Capacidade máxima de jogadores na sala excedida");
    }
}
