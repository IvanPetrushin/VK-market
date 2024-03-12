package ru.vk_market.usecase

import ru.vk_market.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import ru.vk_market.entities.ItemListResponse
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(skip: Int, limit: Int): Single<ItemListResponse> = repo.getItems(skip, limit)
}