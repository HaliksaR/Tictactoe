package ru.haliksar.tictactoe.domain.entity

import com.squareup.moshi.Json

enum class Marker {
    @Json(name = "O") O,
    @Json(name = "X") X,
}