package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;

@Data
public class RoomCreateDto {
    private String userId;
    private String nickname;
}
