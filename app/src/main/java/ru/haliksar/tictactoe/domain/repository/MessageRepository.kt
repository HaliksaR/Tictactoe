package ru.haliksar.tictactoe.domain.repository

import ru.haliksar.tictactoe.domain.entity.InMessage

interface MessageRepository {

    suspend fun sendMessage(roomId: Long, message: String)

    suspend fun getMessages(roomId: Long): List<InMessage>
}
