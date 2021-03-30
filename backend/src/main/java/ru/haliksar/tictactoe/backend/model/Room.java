package ru.haliksar.tictactoe.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import ru.haliksar.tictactoe.backend.exception.RoomException;

@Data
@Entity
@Table(name = "Room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private RoomStatus roomStatus;

    @ManyToMany
    private List<RoomPlayer> players = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<RoomTable> table = new ArrayList<RoomTable>() {
        @Override
        public boolean add(RoomTable v) {
            if (size() < 10) {
                return super.add(v);
            }
            throw new RoomException("выход за пределы поля");
        }
    };

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<RoomChat> messages;
}
