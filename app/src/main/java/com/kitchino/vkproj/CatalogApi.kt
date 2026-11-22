package com.kitchino.vkproj

import com.kitchino.vkproj.model.App
import kotlinx.coroutines.delay
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<App>

    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: Int): App

    suspend fun getRating(id: Int): Float{
        delay(1500)
        return (30..50).random()/10f
    }
}