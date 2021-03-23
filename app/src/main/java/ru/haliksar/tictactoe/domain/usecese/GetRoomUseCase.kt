package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.repository.RoomRepository

class GetRoomUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(roomId: Long, userId: String): Room =
        repository.get(roomId, userId)
}