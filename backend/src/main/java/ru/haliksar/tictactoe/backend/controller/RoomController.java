package ru.haliksar.tictactoe.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.dto.PlayerNicknameDto;
import ru.haliksar.tictactoe.backend.dto.RoomCreateDto;
import ru.haliksar.tictactoe.backend.dto.RoomDto;
import ru.haliksar.tictactoe.backend.dto.RoomIdDto;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.dto.RoomMessageDto;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
import ru.haliksar.tictactoe.backend.service.RoomService;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    @ApiOperation("Создание комнаты")
    public RoomIdDto createRoom(@RequestBody RoomCreateDto roomCreateDto) {
        return roomService.createRoom(roomCreateDto);
    }

    @PostMapping("/setTable")
    @ApiOperation("Сделать ход в комнате")
    public void setTable(@RequestBody RoomTableMoveDto roomTableMoveDto) {
        roomService.setTable(roomTableMoveDto);
    }

    @PostMapping("/get")
    @ApiOperation("Получение комнаты")
    public RoomDto getRoom(@RequestBody RoomLoginDto roomLoginDto) {
        return roomService.getRoomDto(roomLoginDto);
    }

    @PostMapping("/send-message")
    @ApiOperation("Отправка сообщения в комнату")
    public void sendMessage(@RequestBody ChatDto chatDto) {
        roomService.sendMessage(chatDto);
    }

    @GetMapping("/get-messages")
    @ApiOperation("Получение чата комнаты")
    public List<RoomMessageDto> getRoomMessages(@ApiParam(value = "Id комнаты", required = true) @RequestParam(name = "roomId")
                                                        int roomId) {
        return roomService.getRoomMessages(roomId);
    }

    @GetMapping("/get-nickname")
    @ApiOperation("Получение никнейма")
    public PlayerNicknameDto getNickName(@RequestParam String userId) {
        return roomService.getNickName(userId);
    }
}
