package com.kitchino.vkproj.presentation.model

/**
 * UI events for AppListScreen.
 * Using sealed interface to represent one-shot events.
 */
sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}