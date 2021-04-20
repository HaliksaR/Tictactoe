package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
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

private const val PATTERN = "yyyy-MM-dd"

class DateAdapter {
    @ToJson
    fun dateToJson(value: Date): String {
        val format = SimpleDateFormat(PATTERN, Locale.getDefault())
        return format.format(value)
    }

    @FromJson
    fun dateFromJson(value: String): Date {
        val format = SimpleDateFormat(PATTERN, Locale.getDefault())
        return format.parse(value)!!
    }
}