package com.kitchino.vkproj.domain

import com.kitchino.vkproj.AppsRepository
import com.kitchino.vkproj.model.App
import javax.inject.Inject

class GetAppsUseCase @Inject constructor(
    private val repository: AppsRepository,
) {
    suspend operator fun invoke(): List<App> = repository.getApp()
}