package ru.zakoulov.vkcupg.vkutils

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.exceptions.VKApiException
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

abstract class VKRequestGson<T>(
    protected val gson: Gson,
    protected val jsonParser: JsonParser,
    method: String
) : VKRequest<T>(method) {
    @Throws(VKApiException::class)
    override fun parse(response: String): T {
        try {
            return gsonParse(response)
        } catch (e: Throwable) {
            throw VKApiExecutionException(ERROR_MALFORMED_RESPONSE, method, true,
                "[$method] ${e.localizedMessage}")
        }
    }

    @Deprecated("I wanna use gson", ReplaceWith("gsonParse(response: String)"), DeprecationLevel.ERROR)
    override fun parse(r: JSONObject): T {
        return super.parse(r)
    }

    abstract fun gsonParse(response: String): T
}
