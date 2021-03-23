package ru.haliksar.tictactoe.data.api

import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.haliksar.tictactoe.domain.entity.Room

interface RoomApi {

    @POST("room/create")
    suspend fun create(@Field("userId") userId: String): Long

    @GET("room/get")
    suspend fun get(
        @Query("roomId") roomId: Long,
        @Query("userId") userId: String,
    ): Room

    @POST("room/setTable")
    fun setTable(
        @Query("roomId") roomId: Long,
        @Query("userId") userId: String,
        @Field("index") index: Int
    ): Room
}