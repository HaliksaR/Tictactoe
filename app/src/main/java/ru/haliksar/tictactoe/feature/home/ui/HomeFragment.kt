package ru.haliksar.tictactoe.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.haliksar.tictactoe.R
import ru.haliksar.tictactoe.core.*
import ru.haliksar.tictactoe.databinding.HomeFragmentBinding
import ru.haliksar.tictactoe.feature.home.presentation.HomeState
import ru.haliksar.tictactoe.feature.home.presentation.HomeViewModel

class HomeFragment : BindingFragment<HomeFragmentBinding>() {

    private val viewModel by viewModel<HomeViewModel>()

    private val _actionFlow = MutableSharedFlow<HomeAction>()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = HomeFragmentBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        viewModel.subscribeAction(_actionFlow.asSharedFlow())
        subscribeState(viewModel.stateFlow)
        subscribeSideEffect(viewModel.sideEffectFlow)
        subscribeInput()
        subscribeBtn()
    }

    private fun subscribeBtn() {
        binding.roomBtn.bindClick()
            .onEach {
                val roomId = binding.roomIdInput.text?.toString()?.toLongOrNull()
                if (roomId != null) {
                    _actionFlow.emit(HomeAction.GoToRoom(roomId))
                } else {
                    _actionFlow.emit(HomeAction.CreateRoom)
                }
            }.launchWhenStarted(lifecycleScope)
    }

    private fun subscribeInput() {
        binding.roomIdInput.asFlow()
            .onEach {
                _actionFlow.emit(HomeAction.RoomInput(it))
            }.launchWhenStarted(lifecycleScope)
    }

    private fun subscribeState(states: Flow<HomeState>) {
        states.onEach { state ->
            when (state) {
                is HomeState.CreateRoomSuccess -> {
                    contentEnable(true)
                    binding.roomIdInput.setText(state.roomId.toString())
                }
                is HomeState.GetRoomSuccess -> {
                    contentEnable(true)
                }
                is HomeState.Loading -> {
                    contentEnable(false)
                    toast(state.message)
                }
                HomeState.AccessJoin -> {
                    contentEnable(true)
                    binding.roomBtn.text = getString(R.string.access_join)
                }
                HomeState.Idle -> {
                    contentEnable(true)
                    binding.roomBtn.text = getString(R.string.create_room)
                }
                is HomeState.Error -> {
                    contentEnable(true)
                    toast(state.message)
                }
                is HomeState.NavigateToRoom -> {
                    navigateSafe(R.id.action_home_to_room, currentDestination = R.id.home) {
                        putLong("ROOM_ID", state.roomId)
                    }
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun subscribeSideEffect(sideEffects: Flow<HomeSideEffect>) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is HomeSideEffect.ShowToast -> {
                    toast("ShowToast")
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun contentEnable(status: Boolean) {
        binding.roomBtn.isEnabled = status
        binding.roomIdInput.isEnabled = status
    }
}