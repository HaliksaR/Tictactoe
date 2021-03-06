package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import ru.haliksar.tictactoe.backend.model.SynchronizationSetTable;

@Repository
public interface SynchronizationSetTableRepository extends JpaRepository<SynchronizationSetTable, Integer> {
    List<SynchronizationSetTable> findAllByServerPort(int serverPort);
}
