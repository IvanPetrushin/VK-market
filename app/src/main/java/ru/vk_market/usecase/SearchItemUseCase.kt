package ru.vk_market.usecase

import ru.vk_market.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import ru.vk_market.entities.ItemListResponse
import javax.inject.Inject

class SearchItemUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(query: String, skip: Int, limit: Int): Single<ItemListResponse> =
        repo.searchItems(query, skip, limit)
}