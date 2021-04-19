package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class ChatDto {
    private String userId;
    private long roomId;
    private String message;
}
