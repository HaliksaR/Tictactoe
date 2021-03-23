package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json


data class Room(
    @Json(name = "id") val id: Long,
    @Json(name = "status") val status: RoomStatus,
    @Json(name = "players") val players: List<RoomPlayer>,
    @Json(name = "table") val table: List<Marker?>
)

var mockRoom =
    Room(
        id = 122,
        status = RoomStatus.ACTIVE,
        players = listOf(RoomPlayer(id = "4345", Marker.O), RoomPlayer(id = "4345", Marker.X)),
        table = listOf(
            Marker.O, Marker.O, Marker.X,
            null, null, null,
            null, null, null,
        )
    )