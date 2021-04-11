package ru.haliksar.tictactoe.feature.room.presentation

import androidx.annotation.StringRes
import ru.haliksar.tictactoe.domain.entity.Marker
import ru.haliksar.tictactoe.domain.entity.RoomPlayer

sealed class RoomState {
    data class Error(val message: String?) : RoomState()
    data class RunRoom(val table: List<Marker?>, val players: Pair<RoomPlayer, RoomPlayer?>) : RoomState()
    data class WinRoom(val winners: Pair<RoomPlayer?, RoomPlayer?>) : RoomState()
    data class Loading(@StringRes val message: Int) : RoomState()
    object NavigateBack : RoomState()
}
