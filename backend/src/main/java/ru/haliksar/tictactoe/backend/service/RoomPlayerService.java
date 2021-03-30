package ru.haliksar.tictactoe.backend.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.haliksar.tictactoe.backend.model.Marker;
import ru.haliksar.tictactoe.backend.model.RoomPlayer;
import ru.haliksar.tictactoe.backend.repository.RoomPlayerRepository;

@Service
@RequiredArgsConstructor
public class RoomPlayerService {

    private final RoomPlayerRepository roomPlayerRepository;

    public RoomPlayer createPlayer(int userId, Marker marker) {

        RoomPlayer roomPlayer = roomPlayerRepository.findById(userId);
        if (roomPlayer == null) {
            RoomPlayer player = new RoomPlayer();
            player.setId(userId);
            player.setMarker(marker);
            return roomPlayerRepository.save(player);
        } else {
            roomPlayer.setMarker(marker);
            return roomPlayerRepository.save(roomPlayer);
        }
    }

    public RoomPlayer getPlayer(int userId) {
        return roomPlayerRepository.findById(userId);
    }

}
