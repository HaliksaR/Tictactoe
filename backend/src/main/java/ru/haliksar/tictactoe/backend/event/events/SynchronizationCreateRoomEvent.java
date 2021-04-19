package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.RoomCreateDto;

public class SynchronizationCreateRoomEvent extends ApplicationEvent {
    public SynchronizationCreateRoomEvent(RoomCreateDto createDto) {
        super(createDto);
    }
}
