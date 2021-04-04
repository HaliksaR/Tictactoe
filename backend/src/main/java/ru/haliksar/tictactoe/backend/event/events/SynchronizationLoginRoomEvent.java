package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;

public class SynchronizationLoginRoomEvent extends ApplicationEvent {

    public SynchronizationLoginRoomEvent(RoomLoginDto roomLoginDto) {
        super(roomLoginDto);
    }
}
