package ru.haliksar.tictactoe.backend.event.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.RoomCreateSyncDto;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationCreateRoomEvent;
import ru.haliksar.tictactoe.backend.service.SynchronizationService;

@Component
@RequiredArgsConstructor
public class SynchronizationCreateRoomListener {

    private final SynchronizationService synchronizationService;

    @EventListener
    public void onSynchronizationCreateRoom(SynchronizationCreateRoomEvent event) {
        RoomCreateSyncDto createDto = (RoomCreateSyncDto) event.getSource();
        synchronizationService.synchronizationCreateRoom(createDto);
    }
}
