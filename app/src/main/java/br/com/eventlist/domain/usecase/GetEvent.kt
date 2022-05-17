package br.com.eventlist.domain.usecase

import br.com.eventlist.data.repository.EventRepository
import br.com.eventlist.domain.model.Event

class GetEvent(
    private val eventRepository: EventRepository
) : GetEventUseCase {

    override suspend fun invoke(id: String): Event? = try {
        eventRepository.getEvent(id)
    } catch (e: Exception) {
        null
    }
}

interface GetEventUseCase {
    suspend operator fun invoke(id: String): Event?
}