package ru.haliksar.tictactoe.core

import org.koin.dsl.module

val CoreModule = module {
    single { provideMoshi() }
    single { provideOkHttpClient() }
    single { Service(get(), get()) }
}