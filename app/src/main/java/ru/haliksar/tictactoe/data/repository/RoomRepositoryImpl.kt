package ru.haliksar.tictactoe.data.repository

import ru.haliksar.tictactoe.data.datasource.RoomRemoteDataSource
import ru.haliksar.tictactoe.data.datasource.UserLocalDataSource
import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val roomDataSource: RoomRemoteDataSource,
    private val userDataSource: UserLocalDataSource,
) : RoomRepository {

    override suspend fun create(): Long =
        roomDataSource.create(
            player = userDataSource.getUser()
        )

    override suspend fun get(roomId: Long): Room =
        roomDataSource.get(
            roomId = roomId,
            player = userDataSource.getUser()
        )

    override suspend fun setTable(roomId: Long, index: Int): Room =
        roomDataSource.setTable(
            roomId = roomId,
            player = userDataSource.getUser(),
            index = index
        )
}