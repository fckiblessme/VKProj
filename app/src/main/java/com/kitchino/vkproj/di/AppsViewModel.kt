package com.kitchino.vkproj.di

import androidx.lifecycle.ViewModel
import com.kitchino.vkproj.AppsRepository
import com.kitchino.vkproj.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppsViewModel  @Inject constructor(
    private val repository: AppsRepository
) : ViewModel(){
    fun loadApps(): List<App> = repository.getApp()
}