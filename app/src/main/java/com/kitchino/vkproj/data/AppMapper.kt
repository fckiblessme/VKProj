package com.kitchino.vkproj.data

import com.kitchino.vkproj.model.App
import javax.inject.Inject

class AppMapper @Inject constructor() {

    fun dtoToApp(dto: AppDto): App = App(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
    )

    fun appToDto(app: App): AppDto = AppDto(
        id = app.id,
        name = app.name,
        description = app.description,
        category = app.category,
    )

    fun dtoListToAppList(dtos: List<AppDto>): List<App> = dtos.map(::dtoToApp)

    fun appListToDtoList(apps: List<App>): List<AppDto> = apps.map(::appToDto)
}