package br.com.eventlist.data.di

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val PREF_KEY = "event-pref"

val sharedPreferencesModule = module {

    single {
        androidApplication()
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }
}