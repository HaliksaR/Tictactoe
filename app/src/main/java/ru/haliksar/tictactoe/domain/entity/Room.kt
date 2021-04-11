package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room(
    @Json(name = "id") val id: Long,
    @Json(name = "players") val players: List<RoomPlayer>,
    @Json(name = "status") val status: Status,
    @Json(name = "table") val table: List<Marker?>
)

@JsonClass(generateAdapter = true)
data class RoomId(
    @Json(name = "roomId") val roomId: Long,
)

enum class Status {
    @Json(name = "ACTIVE")
    ACTIVE,
    @Json(name = "FULL")
    FULL,
    @Json(name = "WIN")
    WIN
}

enum class Marker {
    @Json(name = "O")
    O,
    @Json(name = "X")
    X,
}