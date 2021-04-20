package ru.haliksar.tictactoe.feature.chat.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.feature.chat.presentation.RoomMessageViewModel

val RoomChatModule = module {
    viewModel { (roomId: Long) ->
        RoomMessageViewModel(
            roomId = roomId,
            getRoomMessagesUseCase = get(),
            sendRoomMessageUseCase = get()
        )
    }
}