package ru.vk_market.repository

import io.reactivex.rxjava3.core.Single
import ru.vk_market.entities.ItemListResponse

interface ItemsRepository {

    fun getItems(skip: Int, limit: Int): Single<ItemListResponse>

    fun searchItems(query: String, skip: Int, limit: Int): Single<ItemListResponse>

    fun getCategories(): Single<List<String>>

    fun getItemsByCategory(category: String): Single<ItemListResponse>
}