package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RoomPlayer(
    @Json(name = "id") val id: String,
    @Json(name = "marker") val marker: Marker
)
