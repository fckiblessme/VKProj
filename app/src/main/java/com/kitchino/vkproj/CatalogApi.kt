package com.kitchino.vkproj

import com.kitchino.vkproj.data.AppDto
import kotlinx.coroutines.delay
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<AppDto>

    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: Int): AppDto

    suspend fun getRating(id: Int): Float {
        delay(1500)
        return (30..50).random() / 10f
    }
}