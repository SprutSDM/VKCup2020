package ru.zakoulov.vkcupa.data.source.vk.responses

import com.google.gson.annotations.SerializedName
import ru.zakoulov.vkcupa.data.source.vk.models.VkDocument

data class DocumentsResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<VkDocument>
)
