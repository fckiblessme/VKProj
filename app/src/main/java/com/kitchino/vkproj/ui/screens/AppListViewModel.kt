package com.kitchino.vkproj.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.model.AppRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Состояние экрана «Список приложений».
 *
 * @param apps список приложений для отображения
 * @param isLoading признак того, что данные ещё загружаются
 */
data class AppListUiState(
    val apps: List<App> = emptyList(),
    val isLoading: Boolean = true,
)

/**
 * Единоразовые события экрана.
 *
 * События передаются через [Channel], а не хранятся в состоянии: после того
 * как событие обработано, оно исчезает. Поэтому snackbar не показывается
 * повторно после переворота экрана.
 */
sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

/**
 * ViewModel presentation-слоя (MVVM) для списка приложений.
 *
 * Список загружается один раз в [init]. ViewModel переживает смену
 * конфигурации (поворот экрана): корутина в [viewModelScope] продолжает
 * работу, а состояние [uiState] сохраняется. Повторной загрузки и повторного
 * показа snackbar не происходит.
 */
class AppListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _events = Channel<AppListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            Log.d(TAG, "loadApps(): загрузка списка приложений начата")
            // Имитация асинхронного запроса к источнику данных.
            // Загрузка выполняется в viewModelScope и НЕ перезапускается
            // при повороте экрана — ViewModel сохраняется.
            delay(LOAD_DELAY_MS)
            _uiState.update { current ->
                current.copy(apps = AppRepository.sampleApps, isLoading = false)
            }
            Log.d(TAG, "loadApps(): список приложений загружен")
        }
    }

    /** Нажатие на toolbar: отправляет единоразовое событие показа snackbar. */
    fun onToolbarClick() {
        viewModelScope.launch {
            _events.send(AppListEvent.ShowToolbarSnackbar)
        }
    }

    private companion object {
        const val TAG = "AppListViewModel"
        const val LOAD_DELAY_MS = 500L
    }
}