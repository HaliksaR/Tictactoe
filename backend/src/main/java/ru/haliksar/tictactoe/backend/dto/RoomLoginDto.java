package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class RoomLoginDto {
    private int userId;
    private int roomId;
}
