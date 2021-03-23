package ru.haliksar.tictactoe.feature.home.ui

import androidx.annotation.StringRes

sealed class HomeSideEffect {
    data class ShowToast(@StringRes val message: Int): HomeSideEffect()
}