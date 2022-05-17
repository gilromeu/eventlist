package br.com.eventlist.data.repository

import android.content.SharedPreferences
import android.util.Log
import br.com.eventlist.data.api.ApiResponse
import br.com.eventlist.data.api.EventApi
import br.com.eventlist.data.api.parseResponse
import br.com.eventlist.domain.model.CheckIn
import br.com.eventlist.domain.model.Event
import com.google.gson.Gson

class EventRepositoryImpl(
    private val sharedPref: SharedPreferences,
    private val serviceApi: EventApi
) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        return when (val result = (serviceApi.getEvents()).parseResponse()) {
            is ApiResponse.Success -> { result.value }
            is ApiResponse.Failure -> { listOf() }
        }
    }

    override suspend fun getEvent(id: String): Event? {
        return when (val result = serviceApi.getEvent(id).parseResponse()) {
            is ApiResponse.Success -> { result.value }
            is ApiResponse.Failure -> { null }
        }
    }

    override suspend fun checkIn(checkIn: CheckIn): Boolean {
       return try {
            serviceApi.checkIn(checkIn.eventId, checkIn.name, checkIn.email)
            saveCheckIn(checkIn)
            true
        } catch (e: Exception) {
            Log.e(EventRepository::class.java.name, e.toString())
            false
        }
    }

    private fun saveCheckIn(checkIn: CheckIn): Boolean {
        return try {
            val gson = Gson()
            val json = gson.toJson(checkIn)
            with(sharedPref.edit()) {
                putString(checkIn.eventId, json)
                apply()
            }
            true
        } catch (e: Exception) { false }
    }
}

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getEvent(id: String): Event?
    suspend fun checkIn(checkIn: CheckIn): Boolean
}