package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.repository.PlayerRepository

class IsCurrentUserUseCase(
    private val repository: PlayerRepository,
) {

    suspend operator fun invoke(id: String): Boolean =
        repository.isCurrentUser(id)
}