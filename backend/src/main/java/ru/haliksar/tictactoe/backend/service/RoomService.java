package ru.haliksar.tictactoe.backend.service;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.dto.RoomDto;
import ru.haliksar.tictactoe.backend.dto.RoomIdDto;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.dto.RoomMessageDto;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
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
import static ru.haliksar.tictactoe.backend.model.RoomStatus.LEAVE_FIRST;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.LEAVE_SECOND;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.WIN_FIRST;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.WIN_NOTHING;
import static ru.haliksar.tictactoe.backend.model.RoomStatus.WIN_SECOND;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    private final RoomPlayerService roomPlayerService;

    private final RoomDtoTransformService roomDtoTransformService;

    private final RoomTableRepository roomTableRepository;

    private final RoomCharRepository roomCharRepository;

    public Room getRoom(int roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException("Комната не найдена"));
    }

    public RoomIdDto createRoom(int userId) {
        Room room = new Room();
        room.setRoomStatus(ACTIVE);
        RoomPlayer roomPlayer = roomPlayerService.createPlayer(userId, Marker.O);
        room.getPlayers().add(roomPlayer);
        return new RoomIdDto(roomRepository.save(room).getId());
    }

    public void loginRoom(RoomLoginDto roomLoginDto) {
        Room room = getRoom(roomLoginDto.getRoomId());

        if (room.getPlayers().contains(roomPlayerService.getPlayer(roomLoginDto.getUserId()))) {
            throw new RoomException("Данный пользователь уже в комнате");
        }

        if (room.getPlayers().size() >= 2) {
            throw new RoomException("Данная комната уже занята");
        }
        RoomPlayer roomPlayer = roomPlayerService.createPlayer(roomLoginDto.getUserId(), Marker.X);
        room.getPlayers().add(roomPlayer);
        room.setRoomStatus(LEAVE_FIRST);
        roomRepository.save(room);
    }

    public void setTable(RoomTableMoveDto table) {
        if (table.getIndex() >= 9) {
            throw new RoomException("выход за пределы поля");
        }

        Room room = getRoom(table.getRoomId());
        RoomPlayer player = roomPlayerService.getPlayer(table.getUserId());

        if(!room.getPlayers().contains(player)) {
            throw new RoomException("Данный игрок не состоит в этой комнате");
        }

        Marker playerMarker = player.getMarker();

        switch (room.getRoomStatus()) {
            case LEAVE_FIRST:
                if (playerMarker != Marker.O) {
                    throw new RoomException("Не ваш ход!");
                }
                room.getTable().add(setMarker(table.getIndex(), playerMarker, room));
                room.setRoomStatus(LEAVE_SECOND);
                break;
            case LEAVE_SECOND:
                if (playerMarker != Marker.X) {
                    throw new RoomException("Не ваш ход!");
                }
                room.getTable().add(setMarker(table.getIndex(), playerMarker, room));
                room.setRoomStatus(LEAVE_FIRST);
                break;
            case WIN_FIRST:
            case WIN_SECOND:
            case WIN_NOTHING:
                throw new RoomException("Игра в данной комнате окончена");
            default:
                throw new RoomException("Неизвестная ошибка");
        }
        checkWin(room);
        roomRepository.save(room);
    }

    private void checkWin(Room room) {
        List<RoomTable> roomTables = room.getTable()
                .stream()
                .sorted(Comparator.comparing(RoomTable::getIndex))
                .collect(Collectors.toList());

        List<Marker> newMarkers = roomDtoTransformService.getListMarkers(roomTables);

        if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(1) && newMarkers.get(0) == newMarkers.get(2)) {
            setRoomStatus(newMarkers.get(0), room);
        } else if (newMarkers.get(3) != null && newMarkers.get(3) == newMarkers.get(4) && newMarkers.get(3) == newMarkers.get(5)) {
            setRoomStatus(newMarkers.get(3), room);
        } else if (newMarkers.get(6) != null && newMarkers.get(6) == newMarkers.get(7) && newMarkers.get(6) == newMarkers.get(8)) {
            setRoomStatus(newMarkers.get(6), room);
        } else if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(3) && newMarkers.get(0) == newMarkers.get(6)) {
            setRoomStatus(newMarkers.get(0), room);
        } else if (newMarkers.get(1) != null && newMarkers.get(1) == newMarkers.get(4) && newMarkers.get(1) == newMarkers.get(7)) {
            setRoomStatus(newMarkers.get(1), room);
        } else if (newMarkers.get(2) != null && newMarkers.get(2) == newMarkers.get(5) && newMarkers.get(2) == newMarkers.get(8)) {
            setRoomStatus(newMarkers.get(2), room);
        } else if (newMarkers.get(0) != null && newMarkers.get(0) == newMarkers.get(4) && newMarkers.get(0) == newMarkers.get(8)) {
            setRoomStatus(newMarkers.get(0), room);
        } else if (newMarkers.get(2) != null && newMarkers.get(2) == newMarkers.get(4) && newMarkers.get(2) == newMarkers.get(6)) {
            setRoomStatus(newMarkers.get(2), room);
        } else if (roomTables.size() == 8) {
            room.setRoomStatus(WIN_NOTHING);
        }
    }

    private void setRoomStatus(Marker marker, Room room) {
        switch (marker) {
            case O:
                room.setRoomStatus(WIN_FIRST);
                break;
            case X:
                room.setRoomStatus(WIN_SECOND);
                break;
        }
    }

    private RoomTable setMarker(int index, Marker maker, Room room) {
        if(roomTableRepository.findByIndexAndRoom(index, room) == null) {
            RoomTable roomTable = new RoomTable();
            roomTable.setIndex(index);
            roomTable.setMarker(maker);
            roomTable.setRoom(room);
            return roomTable;
        } else {
            throw new RoomException("Данная клетка уже занята");
        }
    }

    public RoomDto getRoomDto(int roomId) {
        Room room = getRoom(roomId);
        return roomDtoTransformService.getRoomDto(room);
    }

    public void sendMessage(ChatDto chatDto) {
        Room room = getRoom(chatDto.getRoomId());
        RoomPlayer player = roomPlayerService.getPlayer(chatDto.getUserId());

        if(!room.getPlayers().contains(player)) {
            throw new RoomException("Данный игрок не состоит в этой комнате");
        }

        RoomChat roomChat = new RoomChat();
        roomChat.setRoom(room);
        roomChat.setUserId(chatDto.getUserId());
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
}
