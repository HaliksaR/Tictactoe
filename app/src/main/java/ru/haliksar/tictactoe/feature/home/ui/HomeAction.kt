package ru.haliksar.tictactoe.feature.home.ui

sealed class HomeAction {
    object CreateRoom : HomeAction()
    data class GoToRoom(val roomId: Long) : HomeAction()
    data class RoomInput(val text: String?) : HomeAction()
}
