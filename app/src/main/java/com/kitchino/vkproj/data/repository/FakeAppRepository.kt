package com.kitchino.vkproj.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideogameAsset
import com.kitchino.vkproj.data.dto.AppDto
import com.kitchino.vkproj.data.mapper.AppMapper
import com.kitchino.vkproj.domain.model.App
import com.kitchino.vkproj.domain.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class FakeAppRepository : AppRepository {
    // Hardcoded data in DTO format
    private val appDtoList = listOf(
        AppDto(id = 1, name = "VK", description = "Социальная сеть и мессенджер", category = "Социальные сети", icon = Icons.Filled.Chat),
        AppDto(id = 2, name = "VK Видео", description = "Смотрите видео, стримы и клипы", category = "Видео", icon = Icons.Filled.VideogameAsset),
        AppDto(id = 3, name = "VK Музыка", description = "Миллионы треков и подкастов", category = "Музыка", icon = Icons.Filled.MusicNote),
        AppDto(id = 4, name = "VK Pay", description = "Платежи, переводы и карты", category = "Финансы", icon = Icons.Filled.ShoppingCart),
        AppDto(id = 5, name = "VK Облако", description = "Хранение фото и документов", category = "Продуктивность", icon = Icons.Filled.Home),
        AppDto(id = 6, name = "VK WorkSpace", description = "Корпоративный мессенджер и задачи", category = "Бизнес", icon = Icons.Filled.Settings),
        AppDto(id = 7, name = "VK Combo", description = "Подписка на все сервисы VK", category = "Подписки", icon = Icons.Filled.CameraAlt),
        AppDto(id = 8, name = "Другие приложения", description = "Все проекты экосистемы", category = "Разное", icon = Icons.Filled.AccountCircle)
    )

    // Convert DTO to Domain model via Mapper
    private val appDomainList = appDtoList.map { AppMapper.toApp(it) }

    override val appFlow: Flow<App> = flow {
        appDomainList.forEach { emit(it) }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(),
        initialValue = appDomainList.first()
    )

    override val appList: Flow<List<App>> = flow {
        emit(appDomainList)
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(),
        initialValue = appDomainList
    )
}