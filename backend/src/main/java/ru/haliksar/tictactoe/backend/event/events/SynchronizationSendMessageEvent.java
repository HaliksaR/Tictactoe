package ru.haliksar.tictactoe.backend.event.events;

import org.springframework.context.ApplicationEvent;

import ru.haliksar.tictactoe.backend.dto.ChatDto;

public class SynchronizationSendMessageEvent extends ApplicationEvent {

    public SynchronizationSendMessageEvent(ChatDto chatDto) {
        super(chatDto);
    }
}
