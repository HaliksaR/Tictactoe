package ru.haliksar.tictactoe.data.repository

import ru.haliksar.tictactoe.data.datasource.RoomRemoteDataSource
import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val dataSource: RoomRemoteDataSource
) : RoomRepository {

    override suspend fun create(userId: String): Long =
        dataSource.create(userId)

    override suspend fun get(roomId: Long, userId: String): Room =
        dataSource.get(roomId, userId)

    override suspend fun setTable(roomId: Long, userId: String, index: Int): Room =
        dataSource.setTable(roomId, userId, index)
}