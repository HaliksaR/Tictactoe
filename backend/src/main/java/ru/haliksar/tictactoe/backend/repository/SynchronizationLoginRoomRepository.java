package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.haliksar.tictactoe.backend.model.SynchronizationLoginRoom;

@Repository
public interface SynchronizationLoginRoomRepository extends JpaRepository<SynchronizationLoginRoom, Integer> {
}
