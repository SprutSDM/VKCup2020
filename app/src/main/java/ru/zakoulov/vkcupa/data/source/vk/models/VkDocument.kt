package ru.zakoulov.vkcupa.data.source.vk.models

import com.google.gson.annotations.SerializedName

data class VkDocument(
    val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    val title: String,
    val size: Long,
    val ext: String,
    val url: String,
    val date: Long,
    val type: Int,
    val preview: VkPreview?,
    val tags: List<String>?
)
