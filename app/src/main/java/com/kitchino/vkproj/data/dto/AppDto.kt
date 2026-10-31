package com.kitchino.vkproj.data.dto

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data Transfer Object for App data.
 * Contains hardcoded application data that will be converted to Domain model via Mapper.
 */
data class AppDto(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val icon: ImageVector = Icons.Filled.AccountCircle
)