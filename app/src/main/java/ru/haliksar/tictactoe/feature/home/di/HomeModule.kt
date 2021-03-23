package ru.haliksar.tictactoe.feature.home.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.haliksar.tictactoe.core.createRetrofitService
import ru.haliksar.tictactoe.data.api.RoomApi
import ru.haliksar.tictactoe.data.datasource.RoomRemoteDataSource
import ru.haliksar.tictactoe.data.datasource.RoomRemoteDataSourceImpl
import ru.haliksar.tictactoe.data.repository.RoomRepositoryImpl
import ru.haliksar.tictactoe.domain.repository.RoomRepository
import ru.haliksar.tictactoe.domain.usecese.CreateRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.GetRoomUseCase
import ru.haliksar.tictactoe.domain.usecese.GetUserIdUseCase
import ru.haliksar.tictactoe.feature.home.presentation.HomeViewModel

val homeModule = module {
    single<RoomApi> { createRetrofitService(get()) }
    single<RoomRemoteDataSource> { RoomRemoteDataSourceImpl(get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single { GetRoomUseCase(get()) }
    single { CreateRoomUseCase(get()) }
    single { GetUserIdUseCase(androidContext()) }
    viewModel { HomeViewModel(get(), get()) }
}