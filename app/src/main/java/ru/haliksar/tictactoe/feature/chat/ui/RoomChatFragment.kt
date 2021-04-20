package ru.haliksar.tictactoe.feature.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.haliksar.tictactoe.core.*
import ru.haliksar.tictactoe.databinding.RoomChatFragmentBinding
import ru.haliksar.tictactoe.feature.chat.presentation.RoomMessageState
import ru.haliksar.tictactoe.feature.chat.presentation.RoomMessageViewModel
import ru.haliksar.tictactoe.feature.chat.presentation.RoomSideEffect

class RoomChatFragment : BindingBottomSheetDialog<RoomChatFragmentBinding>() {

    companion object {

        fun newInstance(roomId: Long): RoomChatFragment =
            RoomChatFragment().apply {
                arguments = Bundle().apply {
                    putLong("ROOM_ID", roomId)
                }
            }
    }


    private val messageViewModel by viewModel<RoomMessageViewModel>(
        parameters = { parametersOf(getArgument("ROOM_ID")) }
    )

    private val _actionFlow = MutableStateFlow<RoomAction>(RoomAction.GetMessages)

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RoomChatFragmentBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        with(binding) {
            messageViewModel.subscribeAction(_actionFlow.asSharedFlow())
            subscribeMessageState(messageViewModel.stateFlow)
            subscribeSideEffect(messageViewModel.sideEffectFlow)
            subscribeBtn()
        }
    }

    private fun RoomChatFragmentBinding.subscribeBtn() {
        enterBtn.bindClick()
            .onEach {
                val message = messageInput.text?.toString()
                if (!message.isNullOrBlank()) {
                    _actionFlow.emit(RoomAction.SendMessage(message))
                    messageInput.setText("")
                }
            }.launchWhenStarted(lifecycleScope)

    }

    private fun RoomChatFragmentBinding.subscribeMessageState(states: Flow<RoomMessageState>) {
        states.onEach { state ->
            when (state) {
                is RoomMessageState.Error -> {
                }
                is RoomMessageState.Loading -> {
                    messageInput.isEnabled = false
                    messages.text = state.message.toString()
                }
                is RoomMessageState.RunMessages -> {
                    messageInput.isEnabled = true
                    messages.text = state.messages.joinToString("\n") {
                        "${it.nickname}\n${it.message}\n\t\t${it.postDate}\n"
                    }
                }
                RoomMessageState.NavigateBack -> {
                    popBackStack()
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }


    private fun subscribeSideEffect(sideEffects: Flow<RoomSideEffect>) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is RoomSideEffect.ShowNavigateDialog -> {
                }
                is RoomSideEffect.ShowToast -> {
                    sideEffect.message?.let(::toast)
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }
}