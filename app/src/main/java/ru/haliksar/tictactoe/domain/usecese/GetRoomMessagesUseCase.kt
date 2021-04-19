package ru.haliksar.tictactoe.domain.usecese

import ru.haliksar.tictactoe.domain.entity.InMessage
import ru.haliksar.tictactoe.domain.repository.MessageRepository

class GetRoomMessagesUseCase(
    private val repository: MessageRepository,
) {

    suspend operator fun invoke(roomId: Long): List<InMessage> =
        repository.getMessages(roomId)
}