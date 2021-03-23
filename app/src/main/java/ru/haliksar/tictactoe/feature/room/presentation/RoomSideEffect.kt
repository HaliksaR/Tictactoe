package ru.haliksar.tictactoe.feature.room.presentation

import androidx.annotation.StringRes

sealed class RoomSideEffect {
    data class ShowNavigateDialog(@StringRes val message: Int) : RoomSideEffect()
}
