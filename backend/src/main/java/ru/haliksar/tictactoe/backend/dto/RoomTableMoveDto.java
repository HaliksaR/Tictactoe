package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class RoomTableMoveDto {
    long roomId;
    String userId;
    int index;
}
