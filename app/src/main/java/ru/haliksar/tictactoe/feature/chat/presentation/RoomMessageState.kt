package ru.haliksar.tictactoe.feature.chat.presentation

import androidx.annotation.StringRes
import ru.haliksar.tictactoe.domain.entity.InMessage

sealed class RoomMessageState {
    data class Error(val message: String?) : RoomMessageState()
    data class RunMessages(val messages: List<InMessage>) : RoomMessageState()
    data class Loading(@StringRes val message: Int) : RoomMessageState()
    object NavigateBack : RoomMessageState()
}