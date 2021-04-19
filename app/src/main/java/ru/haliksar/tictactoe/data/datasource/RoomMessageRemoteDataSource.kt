package ru.haliksar.tictactoe.data.datasource

import kotlinx.coroutines.Dispatchers
import ru.haliksar.tictactoe.core.Service
import ru.haliksar.tictactoe.data.api.RoomMessageApi
import ru.haliksar.tictactoe.domain.entity.InMessage
import ru.haliksar.tictactoe.domain.entity.OutMessage

interface RoomMessageRemoteDataSource {

    suspend fun sendMessage(outMessage: OutMessage)

    suspend fun getMessages(roomId: Long): List<InMessage>
}

class RoomMessageRemoteDataSourceImpl(
    private val service: Service
) : RoomMessageRemoteDataSource {

    override suspend fun sendMessage(outMessage: OutMessage) {
        service(RoomMessageApi::class, Dispatchers.IO) {
            sendMessage(outMessage)
        }
    }

    override suspend fun getMessages(roomId: Long): List<InMessage> =
        service(RoomMessageApi::class, Dispatchers.IO) {
            getMessages(roomId)
        }
}