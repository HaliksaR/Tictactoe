package ru.haliksar.tictactoe.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.haliksar.tictactoe.domain.entity.InMessage
import ru.haliksar.tictactoe.domain.entity.OutMessage

interface RoomMessageApi {

    @POST("room/send-message")
    suspend fun sendMessage(@Body body: OutMessage)

    @GET("room/get-messages")
    suspend fun getMessages(@Query("roomId") roomId: Long): List<InMessage>
}