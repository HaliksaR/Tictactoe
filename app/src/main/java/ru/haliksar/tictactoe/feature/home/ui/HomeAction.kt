package ru.haliksar.tictactoe.feature.home.ui

sealed class HomeAction {
    object SetNickName : HomeAction()
    data class CreateRoom(val nickname: String?) : HomeAction()
    data class GoToRoom(val roomId: Long, val nickname: String?) : HomeAction()
    data class RoomInput(val text: String?) : HomeAction()
}
