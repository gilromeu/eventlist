package br.com.eventlist.domain.usecase

import br.com.eventlist.data.repository.EventRepository
import br.com.eventlist.domain.model.CheckIn

class PostCheckIn(
    private val eventRepository: EventRepository
) : PostCheckInUseCase {

    override suspend fun invoke(checkIn: CheckIn) =
        eventRepository.checkIn(checkIn)
}

interface PostCheckInUseCase {
    suspend operator fun invoke(checkIn: CheckIn): Boolean
}