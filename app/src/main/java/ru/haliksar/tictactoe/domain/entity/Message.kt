package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class OutMessage(
    @Json(name = "userId") val userId: String,
    @Json(name = "roomId") val roomId: Long,
    @Json(name = "message") val message: String,
)

@JsonClass(generateAdapter = true)
data class InMessage(
    @Json(name = "nickname") val nickname: String,
    @Json(name = "message") val message: String,
    @Json(name = "postDate") val postDate: Date,
)