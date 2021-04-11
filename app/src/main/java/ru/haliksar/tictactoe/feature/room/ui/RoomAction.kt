package ru.haliksar.tictactoe.feature.room.ui

sealed class RoomAction {
    object BackPressed : RoomAction()
    object GetRoom : RoomAction()
    data class ChangeTable(val index: Int) : RoomAction()
    object NavigateDialogOk : RoomAction()
    object NavigateDialogCancel : RoomAction()
}
