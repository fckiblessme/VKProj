package com.kitchino.vkproj.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector

data class App(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val icon: ImageVector = Icons.Filled.AccountCircle
)

object AppRepository {
    val sampleApps = listOf(
        App(
            id = 1,
            name = "VK",
            description = "Социальная сеть и мессенджер",
            category = "Социальные сети",
            icon = Icons.Filled.Chat
        ),
        App(
            id = 2,
            name = "VK Видео",
            description = "Смотрите видео, стримы и клипы",
            category = "Видео",
            icon = Icons.Filled.VideogameAsset
        ),
        App(
            id = 3,
            name = "VK Музыка",
            description = "Миллионы треков и подкастов",
            category = "Музыка",
            icon = Icons.Filled.MusicNote
        ),
        App(
            id = 4,
            name = "VK Pay",
            description = "Платежи, переводы и карты",
            category = "Финансы",
            icon = Icons.Filled.ShoppingCart
        ),
        App(
            id = 5,
            name = "VK Облако",
            description = "Хранение фото и документов",
            category = "Продуктивность",
            icon = Icons.Filled.Home
        ),
        App(
            id = 6,
            name = "VK WorkSpace",
            description = "Корпоративный мессенджер и задачи",
            category = "Бизнес",
            icon = Icons.Filled.Settings
        ),
        App(
            id = 7,
            name = "VK Combo",
            description = "Подписка на все сервисы VK",
            category = "Подписки",
            icon = Icons.Filled.CameraAlt
        ),
        App(
            id = 8,
            name = "Другие приложения",
            description = "Все проекты экосистемы",
            category = "Разное",
            icon = Icons.Filled.AccountCircle
        )
    )
}