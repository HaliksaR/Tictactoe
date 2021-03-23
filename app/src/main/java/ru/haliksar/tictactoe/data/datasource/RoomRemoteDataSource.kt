package ru.haliksar.tictactoe.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.haliksar.tictactoe.data.api.RoomApi
import ru.haliksar.tictactoe.domain.entity.Marker
import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.entity.mockRoom

interface RoomRemoteDataSource {

    suspend fun create(userId: String): Long

    suspend fun get(roomId: Long, userId: String): Room

    suspend fun setTable(roomId: Long, userId: String, index: Int): Room
}

class RoomRemoteDataSourceImpl(
    private val api: RoomApi
) : RoomRemoteDataSource {

    override suspend fun create(userId: String): Long =
        withContext(Dispatchers.IO) {
            api.create(userId)
        }

    override suspend fun get(roomId: Long, userId: String): Room =
        withContext(Dispatchers.IO) {
            mockRoom
//            api.get(roomId, userId)
        }

    override suspend fun setTable(roomId: Long, userId: String, index: Int): Room =
        withContext(Dispatchers.IO) {
            val table = mutableListOf<Marker?>().apply {
                addAll(mockRoom.table)
                this[index] = Marker.X
            }
            mockRoom = mockRoom.copy(table = table)
            mockRoom
//            api.setTable(roomId, userId, index)
        }
}