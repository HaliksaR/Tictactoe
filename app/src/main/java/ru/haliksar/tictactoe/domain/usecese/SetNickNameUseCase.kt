package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.repository.PlayerRepository

class SetNickNameUseCase(
    private val repository: PlayerRepository,
) {

    suspend operator fun invoke(value: String) {
        repository.setNickName(value)
    }
}