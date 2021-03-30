package ru.haliksar.tictactoe.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "RoomPlayer")
public class RoomPlayer {
    @Id
    private int id;

    private Marker marker;
}
