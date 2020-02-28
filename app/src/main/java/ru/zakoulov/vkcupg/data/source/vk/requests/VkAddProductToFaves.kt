package ru.zakoulov.vkcupg.data.source.vk.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkAddProductToFaves(
    marketId: Int,
    productId: Int
) : VKRequest<Int>("fave.addProduct") {

    init {
        addParam("owner_id", -marketId)
        addParam("id", productId)
    }

    override fun parse(r: JSONObject): Int {
        return r.getInt("response")
    }
}
