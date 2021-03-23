package ru.haliksar.tictactoe.feature.room.ui

sealed class RoomAction {
    object BackPressed : RoomAction()
    data class GetRoom(val roomId: Long?) : RoomAction()
    data class ChangeTable(val index: Int) : RoomAction()
    object NavigateDialogOk : RoomAction()
}
