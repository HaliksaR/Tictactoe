package ru.haliksar.tictactoe.backend.dto;

import java.util.List;

import lombok.Data;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.RoomStatus;

@Data
public class RoomDto {
    private int id;
    private RoomStatus status;
    private List<RoomPlayerDto> players;
    private List<Marker> table;
}
