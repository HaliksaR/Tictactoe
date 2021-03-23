package ru.haliksar.tictactoe.domain.repository

import ru.haliksar.tictactoe.domain.entity.Room

interface RoomRepository {

    suspend fun create(userId: String): Long

    suspend fun get(roomId: Long, userId: String): Room

    suspend fun setTable(roomId: Long, userId: String, index: Int): Room
}