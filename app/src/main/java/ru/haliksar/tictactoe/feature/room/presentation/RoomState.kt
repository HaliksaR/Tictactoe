package ru.haliksar.tictactoe.feature.room.presentation

import androidx.annotation.StringRes
import ru.haliksar.tictactoe.domain.entity.Room

sealed class RoomState {
    data class Error(@StringRes val message: Int) : RoomState()
    data class UpdateRoom(val room: Room) : RoomState()
    data class Loading(@StringRes val message: Int) : RoomState()
    object NavigateBack : RoomState()
}
