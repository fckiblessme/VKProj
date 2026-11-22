package com.kitchino.vkproj.domain

import com.kitchino.vkproj.AppsRepository
import com.kitchino.vkproj.RatingResult
import javax.inject.Inject

class LoadRatingsUseCase @Inject constructor(
    private val repository: AppsRepository,
) {
    suspend fun sequentially(ids: List<Int>): RatingResult = repository.loadRatingsSequentially(ids)

    suspend fun inParallel(ids: List<Int>): RatingResult = repository.loadRatingsInParallel(ids)
}