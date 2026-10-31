package com.kitchino.vkproj.presentation.model

import com.kitchino.vkproj.domain.model.App

/**
 * UI state for AppListScreen.
 * Used to pass state between AppListScreen and AppListContent.
 */
data class AppListUiState(
    val apps: List<App> = emptyList(),
    val isLoading: Boolean = false
)