package ru.haliksar.tictactoe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import ru.haliksar.tictactoe.backend.model.SynchronizationSendMessage;

@Repository
public interface SynchronizationSendMessageRepository extends JpaRepository<SynchronizationSendMessage, Integer> {
    List<SynchronizationSendMessage> findAllByServerPort(int serverPort);
}
