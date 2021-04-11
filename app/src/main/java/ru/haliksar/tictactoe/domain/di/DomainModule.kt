package ru.haliksar.tictactoe.domain.di

import org.koin.dsl.module
import ru.haliksar.tictactoe.domain.usecese.*

val DomainModule = module {
    single { CreateRoomUseCase(get()) }
    single { GetNickNameUseCase(get()) }
    single { GetRoomUseCase(get()) }
    single { SetNickNameUseCase(get()) }
    single { SetTableItemUseCase(get()) }
    single { IsCurrentUserUseCase(get()) }
}
