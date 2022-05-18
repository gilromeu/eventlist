package br.com.eventlist.data.di

import br.com.eventlist.data.repository.EventRepositoryImpl
import br.com.eventlist.data.repository.LoginRepository
import br.com.eventlist.domain.usecase.GetEvent
import br.com.eventlist.domain.usecase.GetEvents
import br.com.eventlist.domain.usecase.PostCheckIn
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single { EventRepositoryImpl(get(), get()) }

    single { LoginRepository(androidContext(), get()) }

    single { GetEvents(get()) }

    single { GetEvent(get()) }

    single { PostCheckIn(get()) }
}