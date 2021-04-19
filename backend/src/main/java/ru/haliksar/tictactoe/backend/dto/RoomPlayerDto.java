package ru.haliksar.tictactoe.backend.dto;

import lombok.Data;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.RoomPlayer;

@Data
public class RoomPlayerDto {
    private String id;
    private Marker marker;
    private boolean win;
    private boolean move;
    private String nickname;

    public RoomPlayerDto(RoomPlayer roomPlayer) {
        this.id = roomPlayer.getId();
        this.marker = roomPlayer.getMarker();
        this.win = roomPlayer.isWin();
        this.move = roomPlayer.isMove();
        this.nickname = roomPlayer.getNickname();
    }
}
