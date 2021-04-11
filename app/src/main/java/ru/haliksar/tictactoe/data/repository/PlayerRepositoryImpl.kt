package ru.haliksar.tictactoe.data.repository

import ru.haliksar.tictactoe.data.datasource.UserLocalDataSource
import ru.haliksar.tictactoe.data.datasource.UserRemoteDataSource
import ru.haliksar.tictactoe.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : PlayerRepository {

    override suspend fun setNickName(value: String) {
        userLocalDataSource.setNickName(value)
    }

    override suspend fun getNickName(): String? =
        try {
            val userId = userLocalDataSource.getUuid()
            val nickname = userRemoteDataSource.getNickName(userId)
            userLocalDataSource.setNickName(nickname)
            nickname
        } catch (ignore: Throwable) {
            null
        }

    override suspend fun isCurrentUser(id: String): Boolean =
        userLocalDataSource.getUser().userId == id
}