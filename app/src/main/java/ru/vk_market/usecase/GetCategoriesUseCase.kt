package ru.vk_market.usecase

import ru.vk_market.repository.ItemsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repo: ItemsRepository) {
    fun execute(): Single<List<String>> = repo.getCategories()
}