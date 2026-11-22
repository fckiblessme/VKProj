package com.kitchino.vkproj

import com.kitchino.vkproj.domain.GetAppsUseCase
import com.kitchino.vkproj.model.App
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAppsUseCaseTest {

    private class FakeRepository : AppsRepository {
        var getAppCalls = 0
        var shouldThrow = false
        var apps: List<App> = listOf(App(id = 1, name = "VK", description = "d", category = "c"))

        override suspend fun getApp(): List<App> {
            getAppCalls++
            if (shouldThrow) throw RuntimeException("ошибка репозитория")
            return apps
        }

        override suspend fun getRating(id: Int): Float = 4.0f
        override suspend fun loadRatingsSequentially(ids: List<Int>): RatingResult =
            RatingResult(emptyMap(), 0)

        override suspend fun loadRatingsInParallel(ids: List<Int>): RatingResult =
            RatingResult(emptyMap(), 0)
    }

    private val fake = FakeRepository()
    private val useCase = GetAppsUseCase(fake)

    @Test
    fun `returns apps from repository`() = runBlocking {
        val apps = useCase()

        assertEquals(1, apps.size)
        assertEquals("VK", apps[0].name)
    }

    @Test
    fun `returns empty list when repository returns empty`() = runBlocking {
        fake.apps = emptyList()

        assertEquals(emptyList<App>(), useCase())
    }

    @Test
    fun `propagates exception when repository fails`() = runBlocking {
        fake.shouldThrow = true

        try {
            useCase()
            fail("ожидали исключение")
        } catch (e: RuntimeException) {
            assertEquals("ошибка репозитория", e.message)
        }
    }

    @Test
    fun `calls repository on every invocation`() = runBlocking {
        useCase()
        useCase()

        assertEquals(2, fake.getAppCalls)
    }

    @Test
    fun `keeps order of apps from repository`() = runBlocking {
        fake.apps = listOf(
            App(id = 2, name = "B", description = "d", category = "c"),
            App(id = 1, name = "A", description = "d", category = "c"),
        )

        assertEquals(2, useCase()[0].id)
    }
}