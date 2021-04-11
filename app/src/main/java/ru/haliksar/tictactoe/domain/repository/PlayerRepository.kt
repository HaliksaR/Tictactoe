package ru.haliksar.tictactoe.domain.repository

interface PlayerRepository {

    suspend fun setNickName(value: String)

    suspend fun getNickName(): String?

    suspend fun isCurrentUser(id: String): Boolean
}
