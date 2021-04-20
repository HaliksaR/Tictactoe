package ru.haliksar.tictactoe.feature.chat.presentation

import androidx.annotation.StringRes

sealed class RoomSideEffect {
    data class ShowNavigateDialog(@StringRes val message: Int) : RoomSideEffect()
    data class ShowToast(val message: String?) : RoomSideEffect()
}
