package ru.haliksar.tictactoe.backend.utils.dtoTransform;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.RoomDto;
import ru.haliksar.tictactoe.backend.dto.RoomMessageDto;
import ru.haliksar.tictactoe.backend.dto.RoomPlayerDto;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.Room;
import ru.haliksar.tictactoe.backend.model.RoomChat;
import ru.haliksar.tictactoe.backend.model.RoomPlayer;
import ru.haliksar.tictactoe.backend.model.RoomTable;

@Service
@RequiredArgsConstructor
public class RoomDtoTransformService {
    private final ModelMapper modelMapper;

    private final MapperUtils mapperUtils;

    public RoomDto getRoomDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }

    public RoomMessageDto getRoomMessagesDto(RoomChat roomChat) {
        return modelMapper.map(roomChat, RoomMessageDto.class);
    }

    @PostConstruct
    private void configureMapper() {
        modelMapper.createTypeMap(Room.class, RoomDto.class)
                .addMappings(mapper -> {
                    mapper.using(getRoomPlayers())
                            .map(Room::getPlayers, RoomDto::setPlayers);
                    mapper.using(getRoomTable())
                            .map(Room::getTable, RoomDto::setTable);
                    mapper.map(Room::getRoomStatus, RoomDto::setStatus);
                });
    }

    public Converter<List<RoomPlayer>, List<RoomPlayerDto>> getRoomPlayers() {
        return context -> context.getSource()
                .stream()
                .map(this::getRoomPlayer)
                .collect(Collectors.toList());
    }

    private RoomPlayerDto getRoomPlayer(RoomPlayer roomPlayer) {
        return new RoomPlayerDto(roomPlayer);
    }

    public Converter<List<RoomTable>, List<Marker>> getRoomTable() {
        return context -> getListMarkers(context.getSource());
    }

    public List<Marker> getListMarkers(List<RoomTable> roomTables) {
        List<RoomTable> roomTablesOut = roomTables.stream()
                .sorted(Comparator.comparing(RoomTable::getIndex))
                .collect(Collectors.toList());

        List<Marker> newMarkers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            newMarkers.add(null);
        }

        for (RoomTable roomTable : roomTablesOut) {
            newMarkers.set(roomTable.getIndex(), roomTable.getMarker());
        }
        return newMarkers;
    }
}
