package com.kitchino.vkproj.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchino.vkproj.AppsRepository
import com.kitchino.vkproj.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel  @Inject constructor(
    private val repository: AppsRepository
) : ViewModel(){
    private val _apps = MutableStateFlow<List<App>>(emptyList())
    val apps: StateFlow<List<App>> = _apps.asStateFlow()

    init {
        loadApps()
    }
    fun loadApps() {
        viewModelScope.launch {
            try {
                _apps.value = repository.getApp()
            } catch (e: Exception) {

            }
        }

    }
}