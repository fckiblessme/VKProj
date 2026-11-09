package com.kitchino.vkproj

import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.model.AppRepository
import jakarta.inject.Inject

interface AppsRepository {
    suspend fun getApp() : List<App>
}

class AppsRepositoryImpl @Inject constructor(
    private val api: CatalogApi
) : AppsRepository {
    override suspend fun getApp(): List<App> {
        return api.getCatalog()
    }
}

class AppsMapper @Inject constructor() {
    fun map(): List<App>{
        return AppRepository.sampleApps
    }
}