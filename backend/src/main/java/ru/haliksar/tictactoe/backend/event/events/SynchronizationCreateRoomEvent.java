package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

public class SynchronizationCreateRoomEvent extends ApplicationEvent {
    public SynchronizationCreateRoomEvent(String userId) {
        super(userId);
    }
}
