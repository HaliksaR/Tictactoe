package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.haliksar.tictactoe.backend.model.SynchronizationCreateRoom;

@Repository
public interface SynchronizationCreateRoomRepository extends JpaRepository<SynchronizationCreateRoom, Integer> {
}
