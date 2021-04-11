package ru.haliksar.tictactoe.data.datasource

import kotlinx.coroutines.Dispatchers
import ru.haliksar.tictactoe.core.Service
import ru.haliksar.tictactoe.data.api.RoomApi
import ru.haliksar.tictactoe.domain.entity.ChangeTable
import ru.haliksar.tictactoe.domain.entity.LoginRoom
import ru.haliksar.tictactoe.domain.entity.Player
import ru.haliksar.tictactoe.domain.entity.Room

interface RoomRemoteDataSource {

    suspend fun create(player: Player): Long

    suspend fun get(roomId: Long, player: Player): Room

    suspend fun setTable(roomId: Long, player: Player, index: Int): Room
}

class RoomRemoteDataSourceImpl(
    private val service: Service
) : RoomRemoteDataSource {

    override suspend fun create(player: Player): Long =
        service(RoomApi::class, Dispatchers.IO) {
            create(player).roomId
        }

    override suspend fun get(roomId: Long, player: Player): Room =
        service(RoomApi::class, Dispatchers.IO) {
            get(LoginRoom(roomId, player.userId, player.nickname))
        }

    override suspend fun setTable(roomId: Long, player: Player, index: Int): Room =
        service(RoomApi::class, Dispatchers.IO) {
            setTable(ChangeTable(roomId, player.userId, index))
        }
}