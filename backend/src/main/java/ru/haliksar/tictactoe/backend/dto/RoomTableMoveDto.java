package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class RoomTableMoveDto {
    int roomId;
    int userId;
    int index;
}
