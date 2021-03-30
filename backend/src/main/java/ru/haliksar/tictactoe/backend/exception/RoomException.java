package ru.haliksar.tictactoe.backend.exception;

public class RoomException extends RuntimeException {
    public RoomException(String message) {
        super(message);
    }
}