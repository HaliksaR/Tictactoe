package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.RoomPlayer;

@Data
public class RoomPlayerDto {
    private int id;
    private Marker marker;

    public RoomPlayerDto(RoomPlayer roomPlayer) {
        this.id = roomPlayer.getId();
        this.marker = roomPlayer.getMarker();
    }
}
