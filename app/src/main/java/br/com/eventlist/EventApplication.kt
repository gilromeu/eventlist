package br.com.eventlist

import android.app.Application
import br.com.eventlist.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EventApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            androidContext(this@EventApplication)
            modules(appModule)
        }
    }
}