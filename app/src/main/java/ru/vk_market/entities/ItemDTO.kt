package ru.vk_market.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemDTO(
    @SerializedName("id")
    val itemId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("discountPercentage")
    val offerPercentage: Float,
    @SerializedName("rating")
    val itemRating: Float,
    @SerializedName("stock")
    val stockQuantity: Int,
    @SerializedName("brand")
    val itemBrand: String,
    @SerializedName("category")
    val itemCategory: String,
    @SerializedName("thumbnail")
    val itemThumb: String,
    @SerializedName("images")
    val itemImages: List<String>
) : Parcelable

data class ItemListResponse(val products: List<ItemDTO>)
