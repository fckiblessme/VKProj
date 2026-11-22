package com.kitchino.vkproj

import com.kitchino.vkproj.domain.LoadRatingsUseCase
import com.kitchino.vkproj.model.App
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoadRatingsUseCaseTest {

    private class FakeRepository : AppsRepository {
        var shouldThrow = false

        override suspend fun getApp(): List<App> = emptyList()

        override suspend fun getRating(id: Int): Float {
            if (shouldThrow) throw RuntimeException("нет сети")
            return id / 10f
        }

        override suspend fun loadRatingsSequentially(ids: List<Int>): RatingResult =
            RatingResult(ids.associateWith { getRating(it) }, 0)

        override suspend fun loadRatingsInParallel(ids: List<Int>): RatingResult =
            RatingResult(ids.associateWith { getRating(it) }, 0)
    }

    private val fake = FakeRepository()
    private val useCase = LoadRatingsUseCase(fake)

    @Test
    fun `sequentially returns rating for every id`() = runBlocking {
        val result = useCase.sequentially(listOf(1, 2, 3))

        assertEquals(setOf(1, 2, 3), result.ratings.keys)
    }

    @Test
    fun `inParallel returns rating for every id`() = runBlocking {
        val result = useCase.inParallel(listOf(1, 2, 3))

        assertEquals(setOf(1, 2, 3), result.ratings.keys)
    }

    @Test
    fun `sequentially values match fake answers`() = runBlocking {
        val result = useCase.sequentially(listOf(1, 2, 3))

        assertEquals(mapOf(1 to 0.1f, 2 to 0.2f, 3 to 0.3f), result.ratings)
    }

    @Test
    fun `inParallel values match fake answers`() = runBlocking {
        val result = useCase.inParallel(listOf(4, 5))

        assertEquals(mapOf(4 to 0.4f, 5 to 0.5f), result.ratings)
    }

    @Test
    fun `empty ids give empty map`() = runBlocking {
        assertTrue(useCase.sequentially(emptyList()).ratings.isEmpty())
        assertTrue(useCase.inParallel(emptyList()).ratings.isEmpty())
    }

    @Test
    fun `exception propagates from repository`() = runBlocking {
        fake.shouldThrow = true

        try {
            useCase.sequentially(listOf(1))
            fail("ожидали исключение")
        } catch (e: RuntimeException) {
            assertEquals("нет сети", e.message)
        }
    }

    @Test
    fun `time is recorded and non negative`() = runBlocking {
        val result = useCase.inParallel(listOf(1, 2))

        assertTrue(result.timesMs >= 0)
    }
}