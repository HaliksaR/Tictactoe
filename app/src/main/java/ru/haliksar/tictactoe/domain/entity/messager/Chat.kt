package ru.haliksar.tictactoe.domain.entity.messager

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Chat(
    @Json(name = "userId") val userId: String,
    @Json(name = "message") val message: String,
    @Json(name = "postDate") val postDate: Date,
)

