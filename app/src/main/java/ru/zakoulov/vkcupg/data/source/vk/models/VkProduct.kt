package ru.zakoulov.vkcupg.data.source.vk.models

import com.google.gson.annotations.SerializedName

data class VkProduct(
    val id: Int,
    val title: String,
    val description: String,
    val price: VkProductPrice,
    @SerializedName("thumb_photo") val photo: String,
    @SerializedName("is_favorite") val isFavorite: Boolean?
)

data class VkProductPrice(
    val amount: Int,
    val text: String
)

data class VkProductPriceCurrency(
    val id: Int,
    val name: String
)

data class VkProducts(
    val count: Int,
    val items: List<VkProduct>
)
