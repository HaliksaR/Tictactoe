package ru.haliksar.tictactoe.backend.event.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.ChatDto;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationSendMessageEvent;
import ru.haliksar.tictactoe.backend.service.SynchronizationService;

@Component
@RequiredArgsConstructor
public class SynchronizationSendMessageListener {

    private final SynchronizationService synchronizationService;

    @EventListener
    public void onSynchronizationSendMessage(SynchronizationSendMessageEvent event) {
        ChatDto chatDto = (ChatDto) event.getSource();
        synchronizationService.synchronizationSendMessage(chatDto);
    }

}
