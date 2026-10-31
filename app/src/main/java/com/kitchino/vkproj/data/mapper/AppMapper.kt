package com.kitchino.vkproj.data.mapper

import com.kitchino.vkproj.domain.model.App
import com.kitchino.vkproj.data.dto.AppDto

object AppMapper {
    fun toApp(dto: AppDto): App {
        return App(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            category = dto.category,
            icon = dto.icon
        )
    }
}