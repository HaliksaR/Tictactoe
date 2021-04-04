package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ru.haliksar.tictactoe.backend.model.RoomPlayer;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, String> {

    Optional<RoomPlayer> findById(String userId);

}
