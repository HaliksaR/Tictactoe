package ru.haliksar.tictactoe.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.dto.RoomCreateSyncDto;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
import ru.haliksar.tictactoe.backend.service.RoomService;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
public class SyncController {

    private final RoomService roomService;

    @PostMapping("/create")
    @ApiOperation("Создание комнаты")
    public void createRoom(@RequestBody RoomCreateSyncDto createDto) {
        roomService.createRoomSync(createDto);
    }

    @PostMapping("/connect")
    @ApiOperation("Подключение к комнате")
    public void connectRoom(@RequestBody RoomLoginDto roomLoginDto) {
        roomService.loginRoomSync(roomLoginDto);
    }

    @PostMapping("/setTable")
    @ApiOperation("Сделать ход в комнате")
    public void setTable(@RequestBody RoomTableMoveDto roomTableMoveDto) {
        roomService.setTableSync(roomTableMoveDto);
    }

    @PostMapping("/send-message")
    @ApiOperation("Отправка сообщения в комнату")
    public void sendMessage(@RequestBody ChatDto chatDto) {
        roomService.sendMessageSync(chatDto);
    }
}
