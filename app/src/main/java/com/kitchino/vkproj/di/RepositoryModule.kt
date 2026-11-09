package com.kitchino.vkproj.di

import com.kitchino.vkproj.AppsRepository
import com.kitchino.vkproj.AppsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}