package br.com.eventlist.data.di

import br.com.eventlist.data.repository.EventRepository
import br.com.eventlist.data.repository.EventRepositoryImpl
import br.com.eventlist.data.api.EventApi
import br.com.eventlist.data.api.ServiceApi
import br.com.eventlist.domain.usecase.GetEvents
import br.com.eventlist.domain.usecase.GetEventsUseCase
import org.koin.dsl.module

val appModule = module {

    single { ServiceApi().createService(EventApi::class.java) }

   // single<EventRepository> { EventRepositoryImpl(get()) }

    single<GetEventsUseCase> { GetEvents(get()) }

}