package ru.haliksar.tictactoe.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.haliksar.tictactoe.R
import ru.haliksar.tictactoe.core.launchCatching
import ru.haliksar.tictactoe.domain.usecese.CreateRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.GetUserIdUseCase
import ru.haliksar.tictactoe.feature.home.ui.HomeAction
import ru.haliksar.tictactoe.feature.home.ui.HomeSideEffect


class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
) : ViewModel() {

    private val _stateFlow = MutableSharedFlow<HomeState>()
    val stateFlow: SharedFlow<HomeState> = _stateFlow.asSharedFlow()

    private val _sideEffectFlow = MutableSharedFlow<HomeSideEffect>()
    val sideEffectFlow: SharedFlow<HomeSideEffect> = _sideEffectFlow.asSharedFlow()

    private val userId by lazy { getUserIdUseCase() }

    fun subscribeAction(actions: Flow<HomeAction>) {
        actions.onEach { action ->
            when (action) {
                HomeAction.CreateRoom -> createRoom()
                is HomeAction.GoToRoom -> navigateToRoomScreen(action.roomId)
                is HomeAction.RoomInput -> inputTextChange(action.text)
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun navigateToRoomScreen(roomId: Long) {
        _stateFlow.emit(HomeState.NavigateToRoom(roomId))
    }

    private suspend fun inputTextChange(text: String?) {
        if (text.isNullOrBlank()) {
            _stateFlow.emit(HomeState.Idle)
        } else {
            _stateFlow.emit(HomeState.AccessJoin)
        }
    }

    private fun createRoom() {
        viewModelScope.launchCatching({
            _stateFlow.emit(HomeState.Loading(R.string.loading))
            val roomId = createRoomUseCase(userId)
            _stateFlow.emit(HomeState.CreateRoomSuccess(roomId))
        }, ::handleError)
    }

    private suspend fun handleError(throwable: Throwable) {
        _stateFlow.emit(HomeState.Error(R.string.error))
    }
}