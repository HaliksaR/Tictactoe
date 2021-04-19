package ru.haliksar.tictactoe.feature.room.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.feature.room.presentation.RoomMessageViewModel
import ru.haliksar.tictactoe.feature.room.presentation.RoomViewModel

val RoomModule = module {
    viewModel { (roomId: Long) ->
        RoomViewModel(
            roomId = roomId,
            getRoomUseCase = get(),
            setTableItemUseCase = get(),
            isCurrentUserUseCase = get(),
        )
    }
    viewModel { (roomId: Long) ->
        RoomMessageViewModel(
            roomId = roomId,
            getRoomMessagesUseCase = get(),
            sendRoomMessageUseCase = get()
        )
    }
}