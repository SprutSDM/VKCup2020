package ru.zakoulov.vkcupg.data.source.vk.requests

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiException
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts
import ru.zakoulov.vkcupg.vkutils.VKRequestGson
import java.io.IOException

class VkGetProductFaveStatus(
    gson: Gson,
    jsonParser: JsonParser,
    marketId: Int,
    productId: Int
) : VKRequestGson<VkProducts>(gson, jsonParser, "market.getById") {

    init {
        addParam("item_ids", "${-marketId}_${productId}")
    }

    /* hack for change version */
    @Throws(InterruptedException::class, IOException::class, VKApiException::class)
    override fun onExecute(manager: VKApiManager): VkProducts {
        val config = manager.config

        params["lang"] = config.lang
        params["device_id"] = config.deviceId.value
        params["v"] = "5.103"

        return manager.execute(
            VKMethodCall.Builder()
                .args(params)
                .method(method)
                .version("5.103")
                .build(), this)
    }

    override fun gsonParse(response: String): VkProducts {
        return gson.fromJson(jsonParser.parse(response).asJsonObject["response"], VkProducts::class.java)
    }
}
