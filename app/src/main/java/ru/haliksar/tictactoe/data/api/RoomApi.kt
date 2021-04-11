package ru.haliksar.tictactoe.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.haliksar.tictactoe.domain.entity.*
import ru.haliksar.tictactoe.domain.entity.messager.Chat
import ru.haliksar.tictactoe.domain.entity.messager.Message

interface RoomApi {

    @POST("room/create")
    suspend fun create(@Body body : Player): RoomId

    @POST("room/get")
    suspend fun get(@Body body : LoginRoom): Room

    @POST("room/setTable")
    suspend fun setTable(@Body body: ChangeTable): Room

    @POST("room/send-message")
    suspend fun sendMessage(@Body body: Message): Room

    @GET("/room/get-nickname")
    suspend fun getNickname(@Query("userId") userId: String): PlayerNickName

    @GET("room/get-messages")
    suspend fun getMessages(@Query("roomId") roomId: Long): List<Chat>
}