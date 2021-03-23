package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.repository.RoomRepository

class CreateRoomUseCase(
    private val repository: RoomRepository
) {

    suspend operator fun invoke(userId: String): Long =
        repository.create(userId)
}