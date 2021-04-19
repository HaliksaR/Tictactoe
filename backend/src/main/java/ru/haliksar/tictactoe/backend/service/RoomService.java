package ru.haliksar.tictactoe.backend.service;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.dto.PlayerNicknameDto;
import ru.haliksar.tictactoe.backend.dto.RoomCreateDto;
import ru.haliksar.tictactoe.backend.dto.RoomDto;
import ru.haliksar.tictactoe.backend.dto.RoomIdDto;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.dto.RoomMessageDto;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationCreateRoomEvent;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationLoginRoomEvent;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationSendMessageEvent;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationSetTableEvent;
import ru.haliksar.tictactoe.backend.exception.RoomException;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.Room;
import ru.haliksar.tictactoe.backend.model.RoomChat;
import ru.haliksar.tictactoe.backend.model.RoomPlayer;
import ru.haliksar.tictactoe.backend.model.RoomTable;
import ru.haliksar.tictactoe.backend.repository.RoomCharRepository;
import ru.haliksar.tictactoe.backend.repository.RoomRepository;
import ru.haliksar.tictactoe.backend.repository.RoomTableRepository;
import ru.haliksar.tictactoe.backend.utils.dtoTransform.RoomDtoTransformService;

import static ru.haliksar.tictactoe.backend.model.RoomStatus.ACTIVE;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.FULL;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.WIN;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    private final RoomPlayerService roomPlayerService;

    private final RoomDtoTransformService roomDtoTransformService;

    private final RoomTableRepository roomTableRepository;

    private final RoomCharRepository roomCharRepository;

    private final ApplicationEventPublisher eventPublisher;

    public Room getRoom(long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException("Комната не найдена"));
    }

    public RoomIdDto createRoom(RoomCreateDto createDto) {
        eventPublisher.publishEvent(new SynchronizationCreateRoomEvent(createDto));
        return createRoomSync(createDto);
    }

    public RoomIdDto createRoomSync(RoomCreateDto createDto) {
        Room room = new Room();
        room.setRoomStatus(ACTIVE);
        RoomPlayer roomPlayer = roomPlayerService.createPlayer(createDto.getUserId(), createDto.getNickname(), Marker.X, true);
        room.getPlayers().add(roomPlayer);
        return new RoomIdDto(roomRepository.save(room).getId());
    }

    public void loginRoom(RoomLoginDto roomLoginDto) {
        loginRoomSync(roomLoginDto);
        eventPublisher.publishEvent(new SynchronizationLoginRoomEvent(roomLoginDto));
    }

    public void loginRoomSync(RoomLoginDto roomLoginDto) {
        Room room = getRoom(roomLoginDto.getRoomId());

        if (room.getPlayers().contains(roomPlayerService.getPlayer(roomLoginDto.getUserId()))) {
            throw new RoomException("Данный пользователь уже в комнате");
        }

        if (room.getPlayers().size() >= 2) {
            throw new RoomException("Данная комната уже занята");
        }

        RoomPlayer roomPlayer = roomPlayerService.createPlayer(roomLoginDto.getUserId(), roomLoginDto.getNickname(), Marker.O, false);
        room.getPlayers().add(roomPlayer);
        room.setRoomStatus(FULL);
        roomRepository.save(room);
    }

    public void setTable(RoomTableMoveDto table) {
        setTableSync(table);
        eventPublisher.publishEvent(new SynchronizationSetTableEvent(table));
    }

    public void setTableSync(RoomTableMoveDto table) {
        if (table.getIndex() >= 9) {
            throw new RoomException("выход за пределы поля");
        }

        Room room = getRoom(table.getRoomId());

        if (room.getRoomStatus().equals(ACTIVE)) {
            throw new RoomException("Не все игроки еще подключились");
        }

        if (room.getRoomStatus().equals(WIN)) {
            throw new RoomException("Данная игра уже окончена");
        }

        RoomPlayer playerCurrent = null;
        RoomPlayer playerRival = null;

        for (RoomPlayer player : room.getPlayers()) {
            if (player.getId().equals(table.getUserId())) {
                playerCurrent = player;
            } else {
                playerRival = player;
            }
        }

        RoomPlayer playerCheck = roomPlayerService.getPlayer(table.getUserId());

        if (!room.getPlayers().contains(playerCheck) || !playerCheck.equals(playerCurrent)) {
            throw new RoomException("Данный игрок не состоит в этой комнате");
        }

        Marker playerMarker = playerCurrent.getMarker();

        if (playerCurrent.isMove()) {
            room.getTable().add(setMarker(table.getIndex(), playerMarker, room));
            playerCurrent.setMove(false);
            playerRival.setMove(true);
            updateGetRoomTime(room, playerMarker);
        } else {
            throw new RoomException("Не ваш ход!");
        }

        checkWin(room, playerCurrent);
        roomRepository.save(room);
    }

    private void checkWin(Room room, RoomPlayer player) {
        List<RoomTable> roomTables = room.getTable()
                .stream()
                .sorted(Comparator.comparing(RoomTable::getIndex))
                .collect(Collectors.toList());

        List<Marker> newMarkers = roomDtoTransformService.getListMarkers(roomTables);

        if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(1) && newMarkers.get(0) == newMarkers.get(2)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(3) != null && newMarkers.get(3) == newMarkers.get(4) && newMarkers.get(3) == newMarkers.get(5)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(6) != null && newMarkers.get(6) == newMarkers.get(7) && newMarkers.get(6) == newMarkers.get(8)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(3) && newMarkers.get(0) == newMarkers.get(6)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(1) != null && newMarkers.get(1) == newMarkers.get(4) && newMarkers.get(1) == newMarkers.get(7)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(2) != null && newMarkers.get(2) == newMarkers.get(5) && newMarkers.get(2) == newMarkers.get(8)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(4) && newMarkers.get(0) == newMarkers.get(8)) {
            setRoomStatus(room, player);
        } else if (newMarkers.get(2) != null && newMarkers.get(2) == newMarkers.get(4) && newMarkers.get(2) == newMarkers.get(6)) {
            setRoomStatus(room, player);
        } else if (roomTables.size() == 8) {
            setRoomNothingStatus(room);
        }
    }

    private void setRoomNothingStatus(Room room) {
        room.setRoomStatus(WIN);
        for (RoomPlayer player : room.getPlayers()) {
            player.setWin(true);
        }
    }

    private void setRoomStatus(Room room, RoomPlayer player) {
        room.setRoomStatus(WIN);
        player.setWin(true);
    }

    private RoomTable setMarker(int index, Marker maker, Room room) {
        if (roomTableRepository.findByIndexAndRoom(index, room) == null) {
            RoomTable roomTable = new RoomTable();
            roomTable.setIndex(index);
            roomTable.setMarker(maker);
            roomTable.setRoom(room);
            return roomTable;
        } else {
            throw new RoomException("Данная клетка уже занята");
        }
    }

    public RoomDto getRoomDto(RoomLoginDto roomLoginDto) {
        Room room = getRoom(roomLoginDto.getRoomId());
        if (!room.getPlayers().contains(roomPlayerService.getPlayer(roomLoginDto.getUserId())) && room.getRoomStatus() != WIN) {
            loginRoom(roomLoginDto);
        }
        RoomPlayer roomPlayer = roomPlayerService.getPlayer(roomLoginDto.getUserId());
        updateGetRoomTime(room, roomPlayer.getMarker());
        checkLeaveRoom(room);
        return roomDtoTransformService.getRoomDto(getRoom(roomLoginDto.getRoomId()));
    }

    private void checkLeaveRoom(Room room) {
        if (room.getRoomStatus().equals(FULL)) {
            RoomPlayer player1 = null;
            RoomPlayer player2 = null;

            for (RoomPlayer player : room.getPlayers()) {
                if (player.getMarker().equals(Marker.X)) {
                    player1 = player;
                } else {
                    player2 = player;
                }
            }

            Date checkDate = new Date(Clock.systemUTC().millis());

            if (checkDate.getTime() - room.getGetPlayer1().getTime() > 60000) {
                player2.setWin(true);
                room.setRoomStatus(WIN);
                room.getPlayers().remove(player1);
                return;
            }
            if (checkDate.getTime() - room.getGetPlayer2().getTime() > 60000) {
                player1.setWin(true);
                room.setRoomStatus(WIN);
                room.getPlayers().remove(player2);
            }
        }
    }

    private void updateGetRoomTime(Room room, Marker marker) {
        switch (marker) {
            case X:
                room.setGetPlayer1(new Date(Clock.systemUTC().millis()));
                break;
            case O:
                room.setGetPlayer2(new Date(Clock.systemUTC().millis()));
                break;
        }
        roomRepository.save(room);
    }

    public void sendMessage(ChatDto chatDto) {
        sendMessageSync(chatDto);
        eventPublisher.publishEvent(new SynchronizationSendMessageEvent(chatDto));
    }

    public void sendMessageSync(ChatDto chatDto) {
        Room room = getRoom(chatDto.getRoomId());
        RoomPlayer player = roomPlayerService.getPlayer(chatDto.getUserId());

        if (!room.getPlayers().contains(player)) {
            throw new RoomException("Данный игрок не состоит в этой комнате");
        }

        RoomChat roomChat = new RoomChat();
        roomChat.setRoom(room);
        roomChat.setUserId(chatDto.getUserId());
        roomChat.setNickname(player.getNickname());
        roomChat.setMessage(chatDto.getMessage());

        roomCharRepository.save(roomChat);
    }

    public List<RoomMessageDto> getRoomMessages(int roomId) {
        Room room = getRoom(roomId);

        return room.getMessages().stream()
                .map(roomDtoTransformService::getRoomMessagesDto)
                .sorted(Comparator.comparing(RoomMessageDto::getPostDate))
                .collect(Collectors.toList());
    }

    public PlayerNicknameDto getNickName(String userId) {
        PlayerNicknameDto playerNicknameDto = new PlayerNicknameDto();
        playerNicknameDto.setNickname(roomPlayerService.getPlayer(userId).getNickname());
        return playerNicknameDto;
    }
}
