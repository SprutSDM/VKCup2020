package ru.zakoulov.vkcupg.data.source.vk.models

import com.google.gson.annotations.SerializedName

data class VkMarket(
    val id: Int,
    val name: String,
    @SerializedName("photo_200") val photo: String,
    @SerializedName("is_closed") val isClosed: Int,
    @SerializedName("is_member") val isMember: Int
)

data class VkMarkets(
    val count: Int,
    val items: List<VkMarket>
)
