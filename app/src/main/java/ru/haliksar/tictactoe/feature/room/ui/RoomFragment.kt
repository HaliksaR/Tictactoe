package ru.haliksar.tictactoe.feature.room.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.haliksar.tictactoe.core.*
import ru.haliksar.tictactoe.databinding.RoomFragmentBinding
import ru.haliksar.tictactoe.domain.entity.Marker
import ru.haliksar.tictactoe.domain.entity.RoomPlayer
import ru.haliksar.tictactoe.feature.room.presentation.RoomSideEffect
import ru.haliksar.tictactoe.feature.room.presentation.RoomState
import ru.haliksar.tictactoe.feature.room.presentation.RoomViewModel

class RoomFragment : BindingFragment<RoomFragmentBinding>() {

    private val viewModel by viewModel<RoomViewModel>(
        parameters = { parametersOf(getArgument("ROOM_ID")) }
    )

    private val _actionFlow = MutableStateFlow<RoomAction>(RoomAction.GetRoom)

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RoomFragmentBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        with(binding) {
            roomId.text = getArgument<Long>("ROOM_ID").toString()
            viewModel.subscribeAction(_actionFlow.asSharedFlow())
            subscribeState(viewModel.stateFlow)
            subscribeSideEffect(viewModel.sideEffectFlow)
            subscribeBtn()
        }
    }

    private fun RoomFragmentBinding.subscribeState(states: Flow<RoomState>) {
        states.onEach { state ->
            when(state) {
                is RoomState.Loading -> {
                    content.isVisible = false
                    contentLoading.isVisible = true
                    contentError.isVisible = false
                    contentWin?.isVisible = false
                    loadingMessage.text = getString(state.message)
                }
                is RoomState.Error -> {
                    content.isVisible = false
                    contentLoading.isVisible = false
                    contentError.isVisible = true
                    contentWin?.isVisible = false
                    errorMessage.text = state.message
                }
                RoomState.NavigateBack -> {
                    popBackStack()
                }
                is RoomState.RunRoom -> {
                    content.isVisible = true
                    contentLoading.isVisible = false
                    contentError.isVisible = false
                    contentWin?.isVisible = false
                    renderTitle(state.players)
                    renderTable(state.table, state.players.first.move)
                }
                is RoomState.WinRoom -> {
                    content.isVisible = false
                    contentLoading.isVisible = false
                    contentError.isVisible = false
                    contentWin?.isVisible = true
                    winMessage?.text = winnerText(state.winners)
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun winnerText(winners: Pair<RoomPlayer?, RoomPlayer?>): String =
        when {
            winners.first != null && winners.second != null -> "Tied!"
            winners.first != null -> "You win!"
            winners.second != null -> "Opponent win!"
            else -> "Error"
        }

    private fun RoomFragmentBinding.renderTitle(players: Pair<RoomPlayer, RoomPlayer?>) {
        currentPlayer.text = playerText(players.first)
        opponentPlayer.text = opponentPlayerText(players.second)
    }

    private fun opponentPlayerText(player: RoomPlayer?): String =
        if (player != null) {
            playerText(player)
        } else {
            "Player opponent: Wait"
        }

    private fun playerText(player: RoomPlayer): String =
        "Player ${player.nickname} [${player.marker}] status : ${getPlayerStatus(player.move)}"

    private fun getPlayerStatus(move: Boolean) : String =
        if (move) {
            "Move"
        } else {
            "Locked"
        }


    private fun renderTable(table: List<Marker?>, enable: Boolean) {
        table.forEachIndexed { index, marker ->
            val id = getBtnId(index + 1)
            if (id != null) {
                val button = view?.findViewById<Button>(id)
                button?.let {
                    it.isEnabled = marker == null && enable
                    it.text = getBtnLabel(marker)
                    it.bindClick().onEach {
                        _actionFlow.emit(RoomAction.ChangeTable(index))
                    }.launchWhenStarted(lifecycleScope)
                }
            }
        }
    }

    private fun getBtnLabel(marker: Marker?): String =
        when (marker) {
            Marker.O -> "O"
            Marker.X -> "X"
            else ->  ""
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
                            _actionFlow.value = RoomAction.NavigateDialogOk
                        }
                        setOnCancelListener {
                            _actionFlow.value = RoomAction.NavigateDialogCancel
                        }
                        setOnDismissListener {
                            _actionFlow.value = RoomAction.NavigateDialogCancel
                        }
                    }
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun RoomFragmentBinding.subscribeBtn() {
        onBackPressed { _actionFlow.emit(RoomAction.BackPressed) }
        roomExit.bindClick()
            .onEach {
                _actionFlow.emit(RoomAction.BackPressed)
            }.launchWhenStarted(lifecycleScope)
        retryBtn.bindClick()
            .onEach {
                _actionFlow.emit(RoomAction.GetRoom)
            }.launchWhenStarted(lifecycleScope)
        exitBtn?.bindClick()
           ?.onEach {
                _actionFlow.emit(RoomAction.BackPressed)
            }?.launchWhenStarted(lifecycleScope)
    }
}