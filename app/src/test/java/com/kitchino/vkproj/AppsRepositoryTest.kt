package com.kitchino.vkproj

import com.kitchino.vkproj.data.AppDto
import com.kitchino.vkproj.data.AppMapper
import com.kitchino.vkproj.model.AppRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AppsRepositoryTest {

    // Фейковый API: не ходит в сеть, отвечает детерминированно — поэтому тесты быстрые
    private class FakeCatalogApi : CatalogApi {
        override suspend fun getCatalog(): List<AppDto> = listOf(
            AppDto(id = 1, name = "VK", description = "Соцсеть", category = "Социальные"),
            AppDto(id = 2, name = "VK Музыка", description = "Треки", category = "Музыка"),
        )

        override suspend fun getAppDetails(id: Int): AppDto =
            AppDto(id = id, name = "App $id", description = "desc", category = "cat")

        override suspend fun getRating(id: Int): Float = id / 10f
    }

    private class ThrowingCatalogApi : CatalogApi {
        override suspend fun getCatalog(): List<AppDto> = throw RuntimeException("нет сети")
        override suspend fun getAppDetails(id: Int): AppDto = throw RuntimeException("нет сети")
        override suspend fun getRating(id: Int): Float = 4.5f
    }

    private val repository = AppsRepositoryImpl(FakeCatalogApi(), AppMapper())
    private val failingRepository = AppsRepositoryImpl(ThrowingCatalogApi(), AppMapper())

    @Test
    fun `getApp maps dtos from api to domain models`() = runBlocking {
        val apps = repository.getApp()

        assertEquals(2, apps.size)
        assertEquals(1, apps[0].id)
        assertEquals("VK", apps[0].name)
    }

    @Test
    fun `getApp falls back to sampleApps when api fails`() = runBlocking {
        assertEquals(AppRepository.sampleApps, failingRepository.getApp())
    }

    @Test
    fun `getRating delegates to api`() = runBlocking {
        assertEquals(0.5f, repository.getRating(5), 0.0001f)
    }

    @Test
    fun `loadRatingsSequentially returns rating for every id`() = runBlocking {
        val result = repository.loadRatingsSequentially(listOf(1, 2, 3))

        assertEquals(setOf(1, 2, 3), result.ratings.keys)
    }

    @Test
    fun `loadRatingsSequentially returns deterministic values`() = runBlocking {
        val result = repository.loadRatingsSequentially(listOf(1, 2, 3))

        assertEquals(mapOf(1 to 0.1f, 2 to 0.2f, 3 to 0.3f), result.ratings)
    }

    @Test
    fun `loadRatingsInParallel returns rating for every id`() = runBlocking {
        val result = repository.loadRatingsInParallel(listOf(1, 2, 3, 4, 5))

        assertEquals(setOf(1, 2, 3, 4, 5), result.ratings.keys)
    }

    @Test
    fun `loadRatingsInParallel values match api answers`() = runBlocking {
        val result = repository.loadRatingsInParallel(listOf(10, 20))

        assertEquals(mapOf(10 to 1.0f, 20 to 2.0f), result.ratings)
    }

    @Test
    fun `empty ids give empty ratings`() = runBlocking {
        assertTrue(repository.loadRatingsSequentially(emptyList()).ratings.isEmpty())
        assertTrue(repository.loadRatingsInParallel(emptyList()).ratings.isEmpty())
    }
}