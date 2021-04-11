package ru.haliksar.tictactoe.domain.repository

import ru.haliksar.tictactoe.domain.entity.Room

interface RoomRepository {

    suspend fun create(): Long

    suspend fun get(roomId: Long): Room

    suspend fun setTable(roomId: Long, index: Int): Room
}