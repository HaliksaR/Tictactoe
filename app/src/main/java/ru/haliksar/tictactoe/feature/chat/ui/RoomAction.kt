package ru.haliksar.tictactoe.feature.chat.ui

sealed class RoomAction {
    object BackPressed : RoomAction()
    object GetMessages : RoomAction()
    data class SendMessage(val message: String) : RoomAction()
}
