package ru.haliksar.tictactoe.feature.room.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.domain.usecese.SetTableItemUseCase
import ru.haliksar.tictactoe.feature.room.presentation.RoomViewModel

val roomModule = module {
    single { SetTableItemUseCase(get()) }
    viewModel { RoomViewModel(get(), get(), get()) }
}