package com.kitchino.vkproj

import com.kitchino.vkproj.model.App
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<App>

    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: Int): App
}