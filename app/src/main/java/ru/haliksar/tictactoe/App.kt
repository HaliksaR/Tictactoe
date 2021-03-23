package ru.haliksar.tictactoe

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.haliksar.tictactoe.core.coreModule
import ru.haliksar.tictactoe.feature.home.di.homeModule
import ru.haliksar.tictactoe.feature.room.di.roomModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(homeModule + roomModule + coreModule)
        }
    }
}