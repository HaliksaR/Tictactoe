package ru.haliksar.tictactoe.feature.home.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.feature.home.presentation.HomeViewModel

val HomeModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
}