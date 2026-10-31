package com.kitchino.vkproj.domain.repository

import com.kitchino.vkproj.domain.model.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    val appFlow: Flow<App>
    val appList: Flow<List<App>>
}