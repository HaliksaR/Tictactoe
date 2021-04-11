package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRoom(
    @Json(name = "roomId") val roomId: Long,
    @Json(name = "userId") val userId: String,
    @Json(name = "nickname") val nickname: String,
)

@JsonClass(generateAdapter = true)
data class ChangeTable(
    @Json(name = "roomId") val roomId: Long,
    @Json(name = "userId") val userId: String,
    @Json(name = "index") val index: Int,
)
