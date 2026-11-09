package com.kitchino.vkproj

import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.model.AppRepository
import jakarta.inject.Inject

interface AppsRepository {
    fun getApp() : List<App>
}

class AppsRepositoryImpl @Inject constructor(
    private val mapper: AppsMapper
) : AppsRepository {
    override fun getApp(): List<App> {
        return mapper.map()
    }
}

class AppsMapper @Inject constructor() {
    fun map(): List<App>{
        return AppRepository.sampleApps
    }
}