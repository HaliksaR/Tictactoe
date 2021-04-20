package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;

public class SynchronizationGetRoomEvent extends ApplicationEvent {
    public SynchronizationGetRoomEvent(RoomLoginDto roomLoginDto) {
        super(roomLoginDto);
    }
}
