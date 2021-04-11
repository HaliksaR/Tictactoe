package ru.haliksar.tictactoe.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.haliksar.tictactoe.core.Service
import ru.haliksar.tictactoe.data.api.RoomApi
import ru.haliksar.tictactoe.domain.entity.LoginRoom

interface UserRemoteDataSource {

    suspend fun getNickName(userId: String): String
}

class UserRemoteDataSourceImpl(
    private val service: Service
) : UserRemoteDataSource {

    override suspend fun getNickName(userId: String): String =
        service(RoomApi::class, Dispatchers.IO) {
            getNickname(userId).value
        }
}