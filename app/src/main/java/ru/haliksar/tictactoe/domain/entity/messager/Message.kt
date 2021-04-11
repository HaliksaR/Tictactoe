package ru.haliksar.tictactoe.domain.entity.messager

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "userId") val userId: String,
    @Json(name = "roomId") val roomId: Long,
    @Json(name = "message") val message: String,
)
