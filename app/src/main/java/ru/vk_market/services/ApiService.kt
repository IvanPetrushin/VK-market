package ru.vk_market.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vk_market.entities.ItemListResponse

interface ApiService {

    @GET("./products")
    fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        ): Single<ItemListResponse>

    @GET("./products/search")
    fun searchProducts(
        @Query("q") query: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Single<ItemListResponse>

    @GET("./products/categories")
    fun getCategories(): Single<List<String>>

    @GET("products/category/{path}")
    fun getCategoryItems(
        @Path("path") category: String
    ): Single<ItemListResponse>

}