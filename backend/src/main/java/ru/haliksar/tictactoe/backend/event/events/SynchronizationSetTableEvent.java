package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;

public class SynchronizationSetTableEvent extends ApplicationEvent {

    public SynchronizationSetTableEvent(RoomTableMoveDto table) {
        super(table);
    }
}
