package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    @Json(name = "userId") val userId: String,
    @Json(name = "nickname") val nickname: String,
)

@JsonClass(generateAdapter = true)
data class RoomPlayer(
    @Json(name = "id") val id: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "marker") val marker: Marker,
    @Json(name = "move") val move: Boolean,
    @Json(name = "win") val win: Boolean
)

@JsonClass(generateAdapter = true)
data class PlayerNickName(
    @Json(name = "nickname") val value: String,
)