package ru.zakoulov.vkcupg.data.source.vk.requests

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiException
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.io.IOException

class VkRemoveProductFromFaves(
    marketId: Int,
    productId: Int
) : VKRequest<Int>("fave.removeProduct") {

    init {
        addParam("owner_id", -marketId)
        addParam("id", productId)
    }

    /* hack for change version */
    @Throws(InterruptedException::class, IOException::class, VKApiException::class)
    override fun onExecute(manager: VKApiManager): Int {
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

    override fun parse(r: JSONObject): Int {
        return r.getInt("response")
    }
}
