package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class RoomLoginDto {
    private String userId;
    private long roomId;
}
