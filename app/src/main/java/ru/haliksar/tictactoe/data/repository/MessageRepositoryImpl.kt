package ru.haliksar.tictactoe.data.repository

import ru.haliksar.tictactoe.data.datasource.RoomMessageRemoteDataSource
import ru.haliksar.tictactoe.data.datasource.UserLocalDataSource
import ru.haliksar.tictactoe.domain.entity.InMessage
import ru.haliksar.tictactoe.domain.entity.OutMessage
import ru.haliksar.tictactoe.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val roomMessageRemoteDataSource: RoomMessageRemoteDataSource,
) : MessageRepository {

    override suspend fun sendMessage(roomId: Long, message: String) {
        roomMessageRemoteDataSource.sendMessage(
            OutMessage(
                userId = userLocalDataSource.getUuid(),
                roomId = roomId,
                message = message,
            )
        )
    }

    override suspend fun getMessages(roomId: Long): List<InMessage> =
        roomMessageRemoteDataSource.getMessages(roomId)
}