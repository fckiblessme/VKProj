package com.kitchino.vkproj.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchino.vkproj.domain.GetAppsUseCase
import com.kitchino.vkproj.domain.LoadRatingsUseCase
import com.kitchino.vkproj.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RatingsState(
    val ratings: Map<Int, Float> = emptyMap(),
    val timeMs: Long? = null,
    val mode: String? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getAppsUseCase: GetAppsUseCase,
    private val loadRatingsUseCase: LoadRatingsUseCase,
) : ViewModel() {
    private val _apps = MutableStateFlow<List<App>>(emptyList())
    val apps: StateFlow<List<App>> = _apps.asStateFlow()

    private val _ratings = MutableStateFlow(RatingsState())
    val ratings: StateFlow<RatingsState> = _ratings.asStateFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            try {
                _apps.value = getAppsUseCase()
            } catch (e: Exception) {
                _apps.value = emptyList()
            }
        }
    }

    fun loadRatingsSequentially() {
        viewModelScope.launch {
            val ids = _apps.value.map { it.id }
            if (ids.isEmpty()) return@launch

            _ratings.update { it.copy(isLoading = true) }
            val result = loadRatingsUseCase.sequentially(ids)
            _ratings.update {
                it.copy(
                    ratings = result.ratings,
                    timeMs = result.timesMs,
                    mode = "последовательно",
                    isLoading = false
                )
            }
        }
    }

    fun loadRatingsInParallel() {
        viewModelScope.launch {
            val ids = _apps.value.map { it.id }
            if (ids.isEmpty()) return@launch

            _ratings.update { it.copy(isLoading = true) }
            val result = loadRatingsUseCase.inParallel(ids)
            _ratings.update {
                it.copy(
                    ratings = result.ratings,
                    timeMs = result.timesMs,
                    mode = "параллельно",
                    isLoading = false
                )
            }
        }
    }
}