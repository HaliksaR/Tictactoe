package ru.haliksar.tictactoe.feature.room.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.haliksar.tictactoe.R
import ru.haliksar.tictactoe.core.launchCatching
import ru.haliksar.tictactoe.domain.usecese.GetRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.GetUserIdUseCase
import ru.haliksar.tictactoe.domain.usecese.SetTableItemUseCase
import ru.haliksar.tictactoe.feature.room.ui.RoomAction

class RoomViewModel(
    private val getRoomUseCase: GetRoomUseCase,
    private val setTableItemUseCase: SetTableItemUseCase,
    getUserIdUseCase: GetUserIdUseCase,
) : ViewModel() {

    private val _stateFlow = MutableSharedFlow<RoomState>()
    val stateFlow: SharedFlow<RoomState> = _stateFlow.asSharedFlow()

    private val _sideEffectFlow = MutableSharedFlow<RoomSideEffect>()
    val sideEffectFlow: SharedFlow<RoomSideEffect> = _sideEffectFlow.asSharedFlow()

    private val _userId = getUserIdUseCase()
    private var _roomId: Long? = null

    private var loopUpdate: Job? = null

    private fun connectLoop() {
        if (loopUpdate == null) {
            loopUpdate = viewModelScope.launchCatching({
                while (true) {
                    Log.d("loopUpdate", "loopUpdate")
                    delay(2000)
                    if (_roomId != null) {
                        val room = getRoomUseCase(_roomId!!, _userId)
                        _stateFlow.emit(RoomState.UpdateRoom(room))
                    }
                }
            }, ::handleError)
        }
    }

    private fun disconnectLoop() {
        loopUpdate?.cancel()
        loopUpdate = null
    }

    fun subscribeAction(actions: Flow<RoomAction>) {
        actions.onEach { action ->
            when (action) {
                RoomAction.BackPressed -> _sideEffectFlow.emit(RoomSideEffect.ShowNavigateDialog(R.string.back_pressed))
                is RoomAction.GetRoom -> loadRoom(action.roomId)
                is RoomAction.ChangeTable -> changeTable(action.index)
                is RoomAction.NavigateDialogOk -> _stateFlow.emit(RoomState.NavigateBack)
            }
        }.launchIn(viewModelScope)
    }

    private fun changeTable(index: Int) {
        viewModelScope.launchCatching({
            _stateFlow.emit(RoomState.Loading(R.string.update_table))
            if (_roomId == null) {
                _stateFlow.emit(RoomState.Error(R.string.room_id_null))
                _sideEffectFlow.emit(RoomSideEffect.ShowNavigateDialog(R.string.room_id_null))
            } else {
                val room = setTableItemUseCase(_roomId!!, _userId, index)
                _stateFlow.emit(RoomState.UpdateRoom(room))
            }
        }, ::handleError)
    }

    private fun loadRoom(roomId: Long?) {
        connectLoop()
        viewModelScope.launchCatching({
            _stateFlow.emit(RoomState.Loading(R.string.loading_room))
            if (roomId == null) {
                _stateFlow.emit(RoomState.Error(R.string.room_id_null))
                _sideEffectFlow.emit(RoomSideEffect.ShowNavigateDialog(R.string.room_id_null))
            } else {
                _roomId = roomId
                val room = getRoomUseCase(roomId, _userId)
                _stateFlow.emit(RoomState.UpdateRoom(room))
            }
        }, ::handleError)
    }


    private suspend fun handleError(throwable: Throwable) {
        disconnectLoop()
        _stateFlow.emit(RoomState.Error(R.string.room_id_null))
    }

    override fun onCleared() {
        disconnectLoop()
        super.onCleared()
    }
}