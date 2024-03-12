package ru.vk_market.utils

sealed class PageMode<T>(
    val data: String? = null
) {
    class Base<T> : PageMode<T>()
    class Search<T>(data: String) : PageMode<T>(data = data)
    class Category<T>(data: String) : PageMode<T>(data = data)
}