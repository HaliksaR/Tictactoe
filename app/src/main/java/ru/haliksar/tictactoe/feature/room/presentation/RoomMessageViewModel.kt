package ru.haliksar.tictactoe.feature.room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.haliksar.tictactoe.core.launchCatching
import ru.haliksar.tictactoe.domain.usecese.GetRoomMessagesUseCase
import ru.haliksar.tictactoe.domain.usecese.SendRoomMessageUseCase
import ru.haliksar.tictactoe.feature.room.ui.RoomAction

class RoomMessageViewModel(
    private val roomId: Long,
    private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
    private val sendRoomMessageUseCase: SendRoomMessageUseCase,
) : ViewModel() {

    private val _stateFlow = MutableSharedFlow<RoomMessageState>()
    val stateFlow: Flow<RoomMessageState> = _stateFlow.asSharedFlow()

    private val _sideEffectFlow = MutableSharedFlow<RoomSideEffect>()
    val sideEffectFlow: Flow<RoomSideEffect> = _sideEffectFlow.asSharedFlow()

    private var loopUpdate: Job? = null

    fun subscribeAction(actions: Flow<RoomAction>) {
        actions.onEach { action ->
/*            when (action) {
                RoomAction.GetRoom -> connectLoop()
                is RoomAction.SendMessage -> sendMessage(action.message)
            }*/
        }.launchIn(viewModelScope)
    }

    private fun sendMessage(message: String) {
        viewModelScope.launchCatching({
            sendRoomMessageUseCase(roomId, message)
        }, ::handleError)
    }

    private fun connectLoop() {
        if (loopUpdate == null) {
            loopUpdate = viewModelScope.launchCatching({
                while (true) {
                    delay(2000)
                    val messages = getRoomMessagesUseCase(roomId)
                    _stateFlow.emit(RoomMessageState.RunMessages(messages))
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
        _sideEffectFlow.emit(RoomSideEffect.ShowToast(throwable.message))
    }

    override fun onCleared() {
        disconnectLoop()
        super.onCleared()
    }
}