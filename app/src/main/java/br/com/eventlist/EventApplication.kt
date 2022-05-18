package br.com.eventlist

import android.app.Application
import br.com.eventlist.data.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EventApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@EventApplication)
            modules(listOf(
                networkModule,
                repositoryModule,
                sharedPreferencesModule,
                viewModelModule
            ))
        }
    }
}