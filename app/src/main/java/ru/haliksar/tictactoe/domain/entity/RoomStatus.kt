package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json

enum class RoomStatus {
    @Json(name = "ACTIVE") ACTIVE,
    @Json(name = "FULL") FULL,
    @Json(name = "EXIT") EXIT,
    @Json(name = "LEAVE_FIRST") LEAVE_FIRST,
    @Json(name = "LEAVE_SECOND") LEAVE_SECOND,
    @Json(name = "WIN_FIRST") WIN_FIRST,
    @Json(name = "WIN_SECOND") WIN_SECOND,
    @Json(name = "WIN_NOTHING") WIN_NOTHING,
}