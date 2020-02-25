package ru.zakoulov.vkcupg.data.source.vk.models

import com.google.gson.annotations.SerializedName

data class VkProduct(
    val id: Int,
    val title: String,
    val description: Int,
    val price: VkProductPrice,
    @SerializedName("thumb_photo") val photo: String
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
    val items: VkProduct
)
