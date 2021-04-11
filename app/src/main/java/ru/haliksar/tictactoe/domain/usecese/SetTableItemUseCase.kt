package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.repository.RoomRepository

class SetTableItemUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(roomId: Long, index: Int): Room =
        repository.setTable(roomId, index)
}