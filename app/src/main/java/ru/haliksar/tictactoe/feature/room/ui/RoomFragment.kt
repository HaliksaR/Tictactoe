package ru.haliksar.tictactoe.feature.room.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.haliksar.tictactoe.core.*
import ru.haliksar.tictactoe.databinding.RoomFragmentBinding
import ru.haliksar.tictactoe.domain.entity.Marker
import ru.haliksar.tictactoe.domain.entity.RoomPlayer
import ru.haliksar.tictactoe.domain.entity.RoomStatus
import ru.haliksar.tictactoe.feature.room.presentation.RoomSideEffect
import ru.haliksar.tictactoe.feature.room.presentation.RoomState
import ru.haliksar.tictactoe.feature.room.presentation.RoomViewModel

class RoomFragment : BindingFragment<RoomFragmentBinding>() {

    private val viewModel by viewModel<RoomViewModel>()

    private val _actionFlow = MutableSharedFlow<RoomAction>()

    init {
        lifecycleScope.launchWhenStarted {
            _actionFlow.emit(RoomAction.GetRoom(arguments?.getLong("ROOM_ID")))
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RoomFragmentBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        onBackPressed { _actionFlow.emit(RoomAction.BackPressed) }
        val roomId = arguments?.getLong("ROOM_ID")
        binding.roomId.text = roomId.toString()
        viewModel.subscribeAction(_actionFlow.asSharedFlow())
        subscribeState(viewModel.stateFlow)
        subscribeSideEffect(viewModel.sideEffectFlow)
        subscribeBtn()
    }

    private fun subscribeBtn() {
        binding.roomExit.bindClick()
            .onEach {
                _actionFlow.emit(RoomAction.BackPressed)
            }.launchWhenStarted(lifecycleScope)
        binding.retryBtn.bindClick()
            .onEach {
                _actionFlow.emit(RoomAction.GetRoom(arguments?.getLong("ROOM_ID")))
            }.launchWhenStarted(lifecycleScope)
    }

    private fun subscribeState(states: Flow<RoomState>) {
        states.onEach { state ->
            when (state) {
                is RoomState.Error -> {
                    binding.content.visibility = View.GONE
                    binding.contentLoading.visibility = View.GONE
                    binding.contentError.visibility = View.VISIBLE
                    binding.errorMessage.text = getString(state.message)
                }

                is RoomState.Loading -> {
                    binding.content.visibility = View.GONE
                    binding.contentLoading.visibility = View.VISIBLE
                    binding.contentError.visibility = View.GONE
                    binding.loadingMessage.text = getString(state.message)
                }

                RoomState.NavigateBack -> popBackStack()

                is RoomState.UpdateRoom -> {
                    renderTable(state.room.table, state.room.status)
                    binding.roomId.text = "Room ID: ${state.room.id}"
                    binding.player1.text = getPlayerText(state.room.players, 0)
                    binding.player2.text = getPlayerText(state.room.players, 1)
                    binding.roomStatus.text = state.room.status.name
                    binding.content.visibility = View.VISIBLE
                    binding.contentLoading.visibility = View.GONE
                    binding.contentError.visibility = View.GONE
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun getPlayerText(players: List<RoomPlayer>, index: Int): String =
        players.getOrNull(index)?.let {
            "Player ${index + 1}: ${it.id} : ${it.marker} "
        } ?: "Player : Wait"

    private fun renderTable(table: List<Marker?>, status: RoomStatus) {
        table.forEachIndexed { index, marker ->
            val id = getBtnId(index + 1)
            if (id != null) {
                val button = view?.findViewById<Button>(id)
                button?.let {
                    it.isEnabled = marker == null && statusForEnable(status)
                    it.text = getBtnLabel(marker)
                    it.bindClick().onEach {
                        _actionFlow.emit(RoomAction.ChangeTable(index))
                    }.launchWhenStarted(lifecycleScope)
                }
            }
        }
    }

    private fun statusForEnable(status: RoomStatus): Boolean =
        status == RoomStatus.ACTIVE || status != RoomStatus.FULL

    private fun getBtnLabel(marker: Marker?): String =
        when (marker) {
            Marker.O -> "O"
            Marker.X -> "X"
            null -> ""
        }


    private fun getBtnId(number: Int): Int? =
        try {
            resources.getIdentifier("bu$number", "id", requireActivity().packageName)
        } catch (ignore: Exception) {
            null
        }

    private fun subscribeSideEffect(sideEffects: Flow<RoomSideEffect>) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is RoomSideEffect.ShowNavigateDialog -> {
                    showDialog {
                        setTitle(sideEffect.message)
                        setPositiveButton("ok") { _, _ ->
                            lifecycleScope.launch {
                                _actionFlow.emit(RoomAction.NavigateDialogOk)
                            }
                        }
                    }
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }
}