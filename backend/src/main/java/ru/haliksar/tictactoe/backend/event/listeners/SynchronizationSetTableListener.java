package ru.haliksar.tictactoe.backend.event.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.dto.RoomTableMoveDto;
import ru.haliksar.tictactoe.backend.event.events.SynchronizationSetTableEvent;
import ru.haliksar.tictactoe.backend.service.SynchronizationService;

@Component
@RequiredArgsConstructor
public class SynchronizationSetTableListener {

    private final SynchronizationService synchronizationService;

    @EventListener
    public void onSynchronizationSetTable(SynchronizationSetTableEvent event) {
        RoomTableMoveDto table = (RoomTableMoveDto) event.getSource();
        synchronizationService.synchronizationSetTable(table);
    }
}
