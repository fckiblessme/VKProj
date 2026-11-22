package com.kitchino.vkproj

import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.model.AppRepository
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.system.measureTimeMillis

data class RatingResult(
    val ratings: Map<Int, Float>,
    val timesMs: Long
)

interface AppsRepository {
    suspend fun getApp(): List<App>
    suspend fun getRating(id: Int): Float
    suspend fun loadRatingsSequentially(ids: List<Int>): RatingResult
    suspend fun loadRatingsInParallel(ids: List<Int>): RatingResult
}

class AppsRepositoryImpl @Inject constructor(
    private val api: CatalogApi
) : AppsRepository {
    override suspend fun getApp(): List<App> {
        // TODO: вернуть api.getCatalog(), когда формат ответа сервера совпадёт с моделью App
        return AppRepository.sampleApps
    }

    override suspend fun getRating(id: Int): Float {
        return api.getRating(id)
    }

    override suspend fun loadRatingsSequentially(ids: List<Int>): RatingResult {
        val ratings = mutableMapOf<Int, Float>()
        val time = measureTimeMillis {
            for (id in ids) {
                ratings[id] = getRating(id)
            }
        }
        return RatingResult(ratings, time)
    }

    override suspend fun loadRatingsInParallel(ids: List<Int>): RatingResult {
        var ratings: Map<Int, Float> = emptyMap()
        val time = measureTimeMillis {
            ratings = coroutineScope {
                ids.map { id -> async { id to getRating(id) } }.awaitAll().toMap()
            }
        }
        return RatingResult(ratings, time)
    }
}

class AppsMapper @Inject constructor() {
    fun map(): List<App> {
        return AppRepository.sampleApps
    }
}