package com.kitchino.vkproj.ui.screens


import com.kitchino.vkproj.model.App

sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data class Content(
        val app: App,
        val installState: InstallState = InstallState.NotInstalled,
    ) : AppDetailsState
}

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState // 0..100
    data object Installed : InstallState
}