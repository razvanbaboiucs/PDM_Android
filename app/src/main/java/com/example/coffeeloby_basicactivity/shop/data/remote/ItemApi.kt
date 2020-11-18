package com.example.coffeeloby_basicactivity.shop.data.remote

import com.example.coffeeloby_basicactivity.core.Api
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem
import retrofit2.Response
import retrofit2.http.*

object ItemApi {

    interface Service {
        @GET("/api/coffee")
        suspend fun find(): List<CoffeeItem>

        @GET("/api/coffee/{id}")
        suspend fun read(@Path("id") itemId: String): CoffeeItem;

        @Headers("Content-Type: application/json")
        @POST("/api/coffee")
        suspend fun create(@Body item: CoffeeItem): CoffeeItem

        @Headers("Content-Type: application/json")
        @PUT("/api/coffee/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: CoffeeItem): CoffeeItem

        @DELETE("/api/coffee/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}