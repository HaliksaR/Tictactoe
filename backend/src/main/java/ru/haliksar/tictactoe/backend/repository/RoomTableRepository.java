package ru.haliksar.tictactoe.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.haliksar.tictactoe.backend.model.Room;
import ru.haliksar.tictactoe.backend.model.RoomTable;

@Repository
public interface RoomTableRepository extends JpaRepository<RoomTable, Integer> {
    RoomTable findByIndexAndRoom(int index, Room room);
}
