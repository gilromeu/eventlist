package br.com.eventlist.data.api

import br.com.eventlist.domain.model.Event
import retrofit2.Response
import retrofit2.http.*

interface EventApi {

    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): Response<Event>

    @FormUrlEncoded
    @POST("checkin")
    suspend fun checkIn(
        @Field("eventId") eventId: String,
        @Field("name") name: String,
        @Field("email") email: String
    )
}