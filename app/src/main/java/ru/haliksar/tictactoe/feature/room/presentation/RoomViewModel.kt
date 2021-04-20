package ru.haliksar.tictactoe.feature.room.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.haliksar.tictactoe.R
import ru.haliksar.tictactoe.core.launchCatching
import ru.haliksar.tictactoe.domain.entity.Room
import ru.haliksar.tictactoe.domain.entity.RoomPlayer
import ru.haliksar.tictactoe.domain.entity.Status
import ru.haliksar.tictactoe.domain.usecese.GetRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.IsCurrentUserUseCase
import ru.haliksar.tictactoe.domain.usecese.SetTableItemUseCase
import ru.haliksar.tictactoe.feature.room.ui.RoomAction
import java.io.EOFException

class RoomViewModel(
    private val roomId: Long,
    private val getRoomUseCase: GetRoomUseCase,
    private val setTableItemUseCase: SetTableItemUseCase,
    private val isCurrentUserUseCase: IsCurrentUserUseCase,
) : ViewModel() {

    private val _stateFlow = MutableSharedFlow<RoomState>()
    val stateFlow: Flow<RoomState> = _stateFlow.asSharedFlow()

    private val _sideEffectFlow = MutableSharedFlow<RoomSideEffect>()
    val sideEffectFlow: Flow<RoomSideEffect> = _sideEffectFlow.asSharedFlow()

    private var loopUpdate: Job? = null

    fun subscribeAction(actions: Flow<RoomAction>) {
        actions.onEach { action ->
            Log.d("subscribeAction", action.toString())
            when (action) {
                RoomAction.BackPressed -> {
                    disconnectLoop()
                    _sideEffectFlow.emit(RoomSideEffect.ShowNavigateDialog(R.string.back_pressed))
                }
                is RoomAction.GetRoom -> loadRoom()
                is RoomAction.ChangeTable -> changeTable(action.index)
                is RoomAction.NavigateDialogOk -> {
                    disconnectLoop()
                    _stateFlow.emit(RoomState.NavigateBack)
                }
                RoomAction.NavigateDialogCancel -> Unit
                RoomAction.ToMessage -> {
                    _stateFlow.emit(RoomState.NavigateTOMessage)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun changeTable(index: Int) {
        viewModelScope.launchCatching({
            _stateFlow.emit(RoomState.Loading(R.string.update_table))
            val room = setTableItemUseCase(roomId, index)
            _stateFlow.emitRoomStatus(room)
        }, ::handleError)
    }

    private fun loadRoom() {
        connectLoop()
        viewModelScope.launchCatching({
            _stateFlow.emit(RoomState.Loading(R.string.loading_room))
            val room = getRoomUseCase(roomId)
            _stateFlow.emitRoomStatus(room)
        }, ::handleError)
    }

    private suspend fun MutableSharedFlow<RoomState>.emitRoomStatus(room: Room) {
        val currentUser = room.players.find { isCurrentUserUseCase(it.id) }
        if (currentUser == null || room.players.size > 2) {
            emit(RoomState.Error("incorrect players"))
            return
        }
        val players: Pair<RoomPlayer, RoomPlayer?> = if (currentUser == room.players[0]) {
            Pair(room.players[0], room.players.getOrNull(1))
        } else {
            Pair(room.players[1], room.players.getOrNull(0))
        }

        val winners = when {
            players.first.win && players.second?.win == true -> players
            players.first.win && players.second?.win != true -> Pair(players.first, null)
            !players.first.win && players.second?.win == true -> Pair(null, players.second)
            else -> Pair(null, null)
        }
        when (room.status) {
            Status.ACTIVE,
            Status.FULL -> {
                emit(RoomState.RunRoom(room.table, players))
            }
            Status.WIN -> {
                emit(RoomState.WinRoom(winners))
            }
        }
    }

    private fun connectLoop() {
        if (loopUpdate == null) {
            loopUpdate = viewModelScope.launchCatching({
                while (true) {
                    delay(2000)
                    val room = getRoomUseCase(roomId)
                    _stateFlow.emitRoomStatus(room)
                }
            }, ::handleError)
        }
    }

    private fun disconnectLoop() {
        try {
            loopUpdate?.cancel()
            loopUpdate = null
        } catch (ignore: Exception) {
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        if (throwable !is EOFException) {
            disconnectLoop()
            Log.e("handleError", throwable.message.toString())
            Log.e("handleError", throwable.stackTraceToString())
            _stateFlow.emit(RoomState.Error(throwable.message))
        }
    }

    override fun onCleared() {
        disconnectLoop()
        super.onCleared()
    }
}