package com.kitchino.vkproj.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchino.vkproj.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state: StateFlow<AppDetailsState> = _state.asStateFlow()

    private var installJob: Job? = null

    // вызывается при открытии экрана — кладём приложение в состояние
    fun load(app: App) {
        installJob?.cancel()
        installJob = null
        _state.value = AppDetailsState.Content(app, InstallState.NotInstalled)
    }

    fun onInstallClick() {
        val current = _state.value
        if (current !is AppDetailsState.Content) return

        if (current.installState is InstallState.Installing) {
            // установка уже идёт -> отменяем джобу и сбрасываем состояние
            installJob?.cancel()
            installJob = null
            _state.update {
                if (it is AppDetailsState.Content) {
                    it.copy(installState = InstallState.NotInstalled)
                } else {
                    it
                }
            }
        } else {
            // запускаем установку как корутину и запоминаем её в installJob
            installJob = viewModelScope.launch {
                emulateInstall()
            }
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 0..100 step 5) {
            delay(150)
            _state.update {
                if (it is AppDetailsState.Content) {
                    it.copy(installState = InstallState.Installing(progress))
                } else {
                    it
                }
            }
        }
        _state.update {
            if (it is AppDetailsState.Content) {
                it.copy(installState = InstallState.Installed)
            } else {
                it
            }
        }
    }
}