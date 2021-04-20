package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.RoomCreateSyncDto;

public class SynchronizationCreateRoomEvent extends ApplicationEvent {
    public SynchronizationCreateRoomEvent(RoomCreateSyncDto createDto) {
        super(createDto);
    }
}
