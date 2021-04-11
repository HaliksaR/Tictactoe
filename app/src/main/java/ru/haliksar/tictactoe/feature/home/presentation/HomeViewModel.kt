package ru.haliksar.tictactoe.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.haliksar.tictactoe.R
import ru.haliksar.tictactoe.core.launchCatching
import ru.haliksar.tictactoe.domain.usecese.CreateRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.GetNickNameUseCase
import ru.haliksar.tictactoe.domain.usecese.SetNickNameUseCase
import ru.haliksar.tictactoe.feature.home.ui.HomeAction
import ru.haliksar.tictactoe.feature.home.ui.HomeSideEffect


class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase,
    private val getNickNameUseCase: GetNickNameUseCase,
    private val setNickNameUseCase: SetNickNameUseCase,
) : ViewModel() {

    private val _stateFlow = MutableSharedFlow<HomeState>()
    val stateFlow: Flow<HomeState> = _stateFlow.asSharedFlow()

    private val _sideEffectFlow = MutableSharedFlow<HomeSideEffect>()
    val sideEffectFlow: Flow<HomeSideEffect> = _sideEffectFlow.asSharedFlow()

    fun subscribeAction(actions: Flow<HomeAction>) {
        actions.onEach { action ->
            when (action) {
                HomeAction.SetNickName -> getNickName()
                is HomeAction.CreateRoom -> createRoom(action.nickname)
                is HomeAction.GoToRoom -> navigateToRoomScreen(action.roomId, action.nickname)
                is HomeAction.RoomInput -> inputTextChange(action.text)
            }
        }.launchIn(viewModelScope)
    }

    private fun getNickName() {
        viewModelScope.launchCatching({
            _stateFlow.emit(HomeState.Loading(R.string.loading))
            val nickname = getNickNameUseCase()
            _stateFlow.emit(HomeState.GetNickNameSuccess(nickname))
        }, ::handleError)
    }

    private suspend fun navigateToRoomScreen(roomId: Long, nickname: String?) {
        if (nickname.isNullOrBlank()) {
            _stateFlow.emit(HomeState.Idle)
            _sideEffectFlow.emit(HomeSideEffect.ShowToast(R.string.incorrect_nickname))
        } else {
            viewModelScope.launchCatching({
                _stateFlow.emit(HomeState.Loading(R.string.loading))
                setNickNameUseCase(nickname)
                _stateFlow.emit(HomeState.NavigateToRoom(roomId))
            }, ::handleError)
        }
    }

    private suspend fun inputTextChange(text: String?) {
        if (text.isNullOrBlank()) {
            _stateFlow.emit(HomeState.Idle)
            _sideEffectFlow.emit(HomeSideEffect.ShowToast(R.string.incorrect_room_id))
        } else {
            _stateFlow.emit(HomeState.AccessJoin)
        }
    }

    private suspend fun createRoom(nickname: String?) {
        if (nickname.isNullOrBlank()) {
            _stateFlow.emit(HomeState.Idle)
            _sideEffectFlow.emit(HomeSideEffect.ShowToast(R.string.incorrect_nickname))
        } else {
            viewModelScope.launchCatching({
                _stateFlow.emit(HomeState.Loading(R.string.loading))
                setNickNameUseCase(nickname)
                val roomId = createRoomUseCase()
                _stateFlow.emit(HomeState.CreateRoomSuccess(roomId))
            }, ::handleError)
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        _stateFlow.emit(HomeState.Error(R.string.error))
    }
}