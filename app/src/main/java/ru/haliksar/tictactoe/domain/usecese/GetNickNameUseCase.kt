package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.repository.PlayerRepository

class GetNickNameUseCase(
    private val repository: PlayerRepository,
) {

    suspend operator fun invoke(): String? =
        repository.getNickName()
}