package ru.haliksar.tictactoe.feature.home.presentation

import androidx.annotation.StringRes
import ru.haliksar.tictactoe.domain.entity.Room

sealed class HomeState {
    object Idle : HomeState()
    object AccessJoin : HomeState()
    data class CreateRoomSuccess(val roomId: Long): HomeState()
    data class GetRoomSuccess(val room: Room): HomeState()
    data class Loading(@StringRes val message: Int): HomeState()
    data class Error(@StringRes val message: Int): HomeState()
    data class  NavigateToRoom(val roomId: Long) : HomeState()
}