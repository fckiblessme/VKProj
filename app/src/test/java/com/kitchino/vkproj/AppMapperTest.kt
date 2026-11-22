package com.kitchino.vkproj

import com.kitchino.vkproj.data.AppDto
import com.kitchino.vkproj.data.AppMapper
import com.kitchino.vkproj.model.App
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AppMapperTest {

    private val mapper = AppMapper()

    @Test
    fun `dto to app maps all fields`() {
        val dto = AppDto(id = 1, name = "VK", description = "Соцсеть", category = "Социальные")

        val app = mapper.dtoToApp(dto)

        assertEquals(1, app.id)
        assertEquals("VK", app.name)
        assertEquals("Соцсеть", app.description)
        assertEquals("Социальные", app.category)
    }

    @Test
    fun `app to dto maps all fields`() {
        val app = App(id = 2, name = "VK Музыка", description = "Треки", category = "Музыка")

        val dto = mapper.appToDto(app)

        assertEquals(
            AppDto(id = 2, name = "VK Музыка", description = "Треки", category = "Музыка"),
            dto
        )
    }

    @Test
    fun `roundtrip dto to app to dto keeps dto equal`() {
        val dto = AppDto(id = 3, name = "VK Pay", description = "Платежи", category = "Финансы")

        val result = mapper.appToDto(mapper.dtoToApp(dto))

        assertEquals(dto, result)
    }

    @Test
    fun `dto list to app list keeps size and order`() {
        val dtos = listOf(
            AppDto(id = 1, name = "A", description = "a", category = "c1"),
            AppDto(id = 2, name = "B", description = "b", category = "c2"),
        )

        val apps = mapper.dtoListToAppList(dtos)

        assertEquals(2, apps.size)
        assertEquals("A", apps[0].name)
        assertEquals("B", apps[1].name)
    }

    @Test
    fun `empty dto list maps to empty app list`() {
        assertEquals(emptyList<App>(), mapper.dtoListToAppList(emptyList()))
    }

    @Test
    fun `app list to dto list keeps size`() {
        val apps = listOf(
            App(id = 1, name = "A", description = "a", category = "c1"),
            App(id = 2, name = "B", description = "b", category = "c2"),
            App(id = 3, name = "C", description = "c", category = "c3"),
        )

        assertEquals(3, mapper.appListToDtoList(apps).size)
    }
}