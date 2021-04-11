package ru.haliksar.tictactoe

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.haliksar.tictactoe.core.CoreModule
import ru.haliksar.tictactoe.data.di.DataModule
import ru.haliksar.tictactoe.domain.di.DomainModule
import ru.haliksar.tictactoe.feature.home.di.HomeModule
import ru.haliksar.tictactoe.feature.room.di.RoomModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(CoreModule)
            modules(DataModule + DomainModule)
            modules(HomeModule)
            modules(RoomModule)
        }
    }
}