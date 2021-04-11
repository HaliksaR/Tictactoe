package ru.haliksar.tictactoe.feature.room.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.domain.usecese.SetTableItemUseCase
import ru.haliksar.tictactoe.feature.room.presentation.RoomViewModel

val RoomModule = module {
    viewModel { (roomId: Long) ->
        RoomViewModel(roomId, get(), get(), get())
    }
}