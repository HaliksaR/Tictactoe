package ru.haliksar.tictactoe.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "RoomTable")
public class RoomTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int index;

    private Marker marker;

    @ManyToOne
    @JoinColumn(name = "Room_id", nullable = false)
    private Room room;
}
