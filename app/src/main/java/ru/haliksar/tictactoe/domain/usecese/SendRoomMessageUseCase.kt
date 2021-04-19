package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.repository.MessageRepository

class SendRoomMessageUseCase(
    private val repository: MessageRepository,
) {

    suspend operator fun invoke(roomId: Long, message: String) {
        repository.sendMessage(roomId, message)
    }
}