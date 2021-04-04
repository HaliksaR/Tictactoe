package ru.haliksar.tictactoe.backend.service;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
import ru.haliksar.tictactoe.backend.model.SynchronizationCreateRoom;
import ru.haliksar.tictactoe.backend.model.SynchronizationLoginRoom;
import ru.haliksar.tictactoe.backend.model.SynchronizationSendMessage;
import ru.haliksar.tictactoe.backend.model.SynchronizationSetTable;
import ru.haliksar.tictactoe.backend.repository.SynchronizationCreateRoomRepository;
import ru.haliksar.tictactoe.backend.repository.SynchronizationLoginRoomRepository;
import ru.haliksar.tictactoe.backend.repository.SynchronizationSendMessageRepository;
import ru.haliksar.tictactoe.backend.repository.SynchronizationSetTableRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizationService {

    private final SynchronizationCreateRoomRepository synchronizationCreateRoomRepository;

    private final SynchronizationLoginRoomRepository synchronizationLoginRoomRepository;

    private final SynchronizationSendMessageRepository synchronizationSendMessageRepository;

    private final SynchronizationSetTableRepository synchronizationSetTableRepository;

    @Value("${localserver.port1}")
    private int port1;

    @Value("${localserver.port2}")
    private int port2;

    @Transactional
    public void synchronizationCreateRoom(String userId) {
        Unirest.setTimeouts(0, 0);

        try {
            Unirest.post("http://localhost:" + port1 + "/sync/create?userId=" + userId).asString();
        } catch (UnirestException e) {
            synchronizationCreateRoomRepository.save(new SynchronizationCreateRoom(userId, port1));
        }
        try {
            Unirest.post("http://localhost:" + port2 + "/sync/create?userId=" + userId).asString();
        } catch (UnirestException e) {
            synchronizationCreateRoomRepository.save(new SynchronizationCreateRoom(userId, port2));
        }
    }

    private void synchronizationCreateRoom() {
        List<SynchronizationCreateRoom> createRoomList = synchronizationCreateRoomRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(SynchronizationCreateRoom::getPostDate))
                .collect(Collectors.toList());
        for (SynchronizationCreateRoom room : createRoomList) {
            try {
                Unirest.post("http://localhost:" + room.getServerPort() + "/sync/create?userId=" + room.getUserId()).asString();
                synchronizationCreateRoomRepository.delete(room);
            } catch (UnirestException e) {
                break;
            }
        }
    }

    @Transactional
    public void synchronizationSetTable(RoomTableMoveDto roomTableMoveDto) {
        Unirest.setTimeouts(0, 0);

        String request = "{\n  \"index\": " + roomTableMoveDto.getIndex()
                + ",\n  \"roomId\": " + roomTableMoveDto.getRoomId() + ",\n  \"userId\": " + roomTableMoveDto.getUserId() + "\n}";

        try {
            Unirest.post("http://localhost:" + port1 + "/sync/setTable")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationSetTableRepository.save(new SynchronizationSetTable(port1, roomTableMoveDto.getUserId(), roomTableMoveDto.getRoomId(), roomTableMoveDto.getIndex()));
        }

        try {
            Unirest.post("http://localhost:" + port2 + "/sync/setTable")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationSetTableRepository.save(new SynchronizationSetTable(port2, roomTableMoveDto.getUserId(), roomTableMoveDto.getRoomId(), roomTableMoveDto.getIndex()));
        }

    }

    private void synchronizationSetTable() {
        List<SynchronizationSetTable> setTableList = synchronizationSetTableRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(SynchronizationSetTable::getPostDate))
                .collect(Collectors.toList());
        for (SynchronizationSetTable synchronizationSetTable : setTableList) {
            try {
                String request = "{\n  \"index\": " + synchronizationSetTable.getIndex()
                        + ",\n  \"roomId\": " + synchronizationSetTable.getRoomId() + ",\n  \"userId\": " + synchronizationSetTable.getUserId() + "\n}";
                Unirest.post("http://localhost:" + synchronizationSetTable.getServerPort() + "/sync/setTable")
                        .header("Content-Type", "application/json")
                        .body(request)
                        .asString();
                synchronizationSetTableRepository.delete(synchronizationSetTable);
            } catch (UnirestException e) {
                break;
            }
        }
    }

    @Transactional
    public void synchronizationSendMessage(ChatDto chatDto) {
        Unirest.setTimeouts(0, 0);

        String request = "{\n  \"message\": \"" + chatDto.getMessage() + "\",\n  \"roomId\": "
                + chatDto.getRoomId() + ",\n  \"userId\": " + chatDto.getUserId() + "\n}";

        try {
            Unirest.post("http://localhost:" + port1 + "/sync/send-message")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationSendMessageRepository.save(new SynchronizationSendMessage(port1, chatDto.getUserId(), chatDto.getRoomId(), chatDto.getMessage()));
        }

        try {
            Unirest.post("http://localhost:" + port2 + "/sync/send-message")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationSendMessageRepository.save(new SynchronizationSendMessage(port2, chatDto.getUserId(), chatDto.getRoomId(), chatDto.getMessage()));
        }
    }

    private void synchronizationSendMessage() {
        List<SynchronizationSendMessage> sendMessageList = synchronizationSendMessageRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(SynchronizationSendMessage::getPostDate))
                .collect(Collectors.toList());

        for (SynchronizationSendMessage sendMessage : sendMessageList) {
            String request = "{\n  \"message\": \"" + sendMessage.getMessage() + "\",\n  \"roomId\": "
                    + sendMessage.getRoomId() + ",\n  \"userId\": " + sendMessage.getUserId() + "\n}";
            try {
                Unirest.post("http://localhost:" + sendMessage.getServerPort() + "/sync/send-message")
                        .header("Content-Type", "application/json")
                        .body(request)
                        .asString();
                synchronizationSendMessageRepository.delete(sendMessage);
            } catch (UnirestException e) {
                break;
            }
        }
    }

    @Transactional
    public void synchronizationLoginRoom(RoomLoginDto roomLoginDto) {
        Unirest.setTimeouts(0, 0);

        String request = "{\n  \"roomId\": " + roomLoginDto.getRoomId() + ",\n  \"userId\": " + roomLoginDto.getUserId() +
                "\n}";
        try {
            Unirest.post("http://localhost:" + port1 + "/sync/connect")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationLoginRoomRepository.save(new SynchronizationLoginRoom(port1, roomLoginDto.getUserId(), roomLoginDto.getRoomId()));
        }

        try {
            Unirest.post("http://localhost:" + port2 + "/sync/connect")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .asString();
        } catch (UnirestException e) {
            synchronizationLoginRoomRepository.save(new SynchronizationLoginRoom(port2, roomLoginDto.getUserId(), roomLoginDto.getRoomId()));
        }
    }

    private void synchronizationLoginRoom() {
        List<SynchronizationLoginRoom> loginRoomList = synchronizationLoginRoomRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(SynchronizationLoginRoom::getPostDate))
                .collect(Collectors.toList());

        for (SynchronizationLoginRoom loginRoom : loginRoomList) {
            String request = "{\n  \"roomId\": " + loginRoom.getRoomId() + ",\n  \"userId\": " + loginRoom.getUserId() +
                    "\n}";
            try {
                Unirest.post("http://localhost:" + loginRoom.getServerPort() + "/sync/connect")
                        .header("Content-Type", "application/json")
                        .body(request)
                        .asString();
                synchronizationLoginRoomRepository.delete(loginRoom);
            } catch (UnirestException e) {
                break;
            }
        }
    }

    @Transactional
    @Scheduled(cron = "${synchronize}")
    public void synchronize() {
        if (!synchronizationCreateRoomRepository.findAll().isEmpty()) {
            synchronizationCreateRoom();
            log.info("synchronized create room");
        }
        if (!synchronizationSetTableRepository.findAll().isEmpty()) {
            synchronizationSetTable();
            log.info("synchronized set table");
        }
        if (!synchronizationSendMessageRepository.findAll().isEmpty()) {
            synchronizationSendMessage();
            log.info("synchronized send message");
        }
        if (!synchronizationLoginRoomRepository.findAll().isEmpty()) {
            synchronizationLoginRoom();
            log.info("synchronized login room");
        }
        log.info("synchronized");
    }

}
