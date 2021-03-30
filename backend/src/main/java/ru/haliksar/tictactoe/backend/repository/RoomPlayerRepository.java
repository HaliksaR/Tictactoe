package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.haliksar.tictactoe.backend.model.RoomPlayer;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, Integer> {

    RoomPlayer findById(int userId);

}
