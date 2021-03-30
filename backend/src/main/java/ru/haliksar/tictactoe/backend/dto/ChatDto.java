package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class ChatDto {
    private int userId;
    private int roomId;
    private String message;
}
