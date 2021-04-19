package ru.haliksar.tictactoe.feature.room.presentation

import androidx.annotation.StringRes
import ru.haliksar.tictactoe.domain.entity.InMessage
import ru.haliksar.tictactoe.domain.entity.Marker
import ru.haliksar.tictactoe.domain.entity.RoomPlayer

sealed class RoomMessageState {
    data class Error(val message: String?) : RoomMessageState()
    data class RunMessages(val messages: List<InMessage>) : RoomMessageState()
    data class Loading(@StringRes val message: Int) : RoomMessageState()
}