package com.kitchino.vkproj.presentation.viewmodel

import com.kitchino.vkproj.domain.model.App
import com.kitchino.vkproj.presentation.model.AppListEvent
import com.kitchino.vkproj.presentation.model.AppListUiState
import com.kitchino.vkproj.domain.repository.AppRepository
import com.kitchino.vkproj.data.repository.FakeAppRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel presentation-слоя для экрана списка приложений.
 *
 * Состояние хранится в [AppListUiState], а события (например, показа Snackbar)
 * передаются через [AppListEvent] с использованием Channel (единоразовое событие).
 *
 * Состояния переживает переворот экрана (ViewModel retained), данные
 * загружаются один раз через [AppRepository].
 */
class AppListViewModel(
    private val appRepository: AppRepository = FakeAppRepository()
) : ViewModel() {

    // Состояние экраса: список приложений и флаг загрузки
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    // Единоразовые события (Snackbar и др.)
    // Channel гарантирует, что событие не переиграется после поворота экрана
    private val _events = Channel<AppListEvent>(Channel.BUFFERED)
    val events = kotlinx.coroutines.flow.channelFlow {
        while (true) {
            send(_events.receive())
        }
    }

    init {
        // Инициализация данных при создании ViewModel
        // ViewModel переживает смену конфигурации (поворот экрана),
        // поэтому загрузка данных происходит только один раз.
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            _uiState.value = AppListUiState(
                apps = appRepository.appList.first(),
                isLoading = false
            )
        }
    }

    /** Обработка клика на toolbar — отправляет единоразовое событие показа Snackbar */
    fun onToolbarClick() {
        viewModelScope.launch {
            _events.send(AppListEvent.ShowToolbarSnackbar)
        }
    }
}