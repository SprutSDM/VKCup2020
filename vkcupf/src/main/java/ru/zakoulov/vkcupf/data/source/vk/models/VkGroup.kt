package ru.zakoulov.vkcupf.data.source.vk.models

import com.google.gson.annotations.SerializedName

data class VkGroup(
    val id: Int,
    @SerializedName("name") val title: String,
    @SerializedName("photo_200") val img: String,
    val description: String,
    @SerializedName("members_count") val membersCount: Int
)
