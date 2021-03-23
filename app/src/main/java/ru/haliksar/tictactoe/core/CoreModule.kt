package ru.haliksar.tictactoe.core

import org.koin.dsl.module

val coreModule = module {
    factory { provideRetrofit(get(), get(), "https://TODO") }
    single { provideMoshi() }
    single { provideOkHttpClient(certificatePinner = get()) }
    single { provideCertificatePinner("TODO", true) }
}