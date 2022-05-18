package br.com.eventlist.domain.usecase

import br.com.eventlist.data.repository.EventRepositoryImpl
import br.com.eventlist.domain.model.Event

class GetEvents(
    private val eventRepository: EventRepositoryImpl
) : GetEventsUseCase {

    override suspend fun invoke(): List<Event> = try {
        eventRepository.getEvents()
    } catch (e: Exception) {
        listOf()
    }
}

interface GetEventsUseCase {
    suspend operator fun invoke(): List<Event>
}