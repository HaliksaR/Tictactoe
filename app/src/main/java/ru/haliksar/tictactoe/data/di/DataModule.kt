package ru.haliksar.tictactoe.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.haliksar.tictactoe.data.datasource.*
import ru.haliksar.tictactoe.data.repository.PlayerRepositoryImpl
import ru.haliksar.tictactoe.data.repository.RoomRepositoryImpl
import ru.haliksar.tictactoe.domain.repository.PlayerRepository
import ru.haliksar.tictactoe.domain.repository.RoomRepository

val DataModule = module {
    single<RoomRemoteDataSource> { RoomRemoteDataSourceImpl(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(androidContext()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }

    single<PlayerRepository> { PlayerRepositoryImpl(get(), get()) }
    single<RoomRepository> { RoomRepositoryImpl(get(), get()) }
}
