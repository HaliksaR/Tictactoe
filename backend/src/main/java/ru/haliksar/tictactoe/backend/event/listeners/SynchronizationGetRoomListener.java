package ru.haliksar.tictactoe.backend.event.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.RoomLoginDto;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationGetRoomEvent;
import ru.haliksar.tictactoe.backend.service.SynchronizationService;

@Component
@RequiredArgsConstructor
public class SynchronizationGetRoomListener {

    private final SynchronizationService synchronizationService;

    @EventListener
    public void onSynchronizationGetRoom(SynchronizationGetRoomEvent event) {
        RoomLoginDto roomLoginDto = (RoomLoginDto) event.getSource();
        synchronizationService.synchronizationGetRoom(roomLoginDto);
    }
}
