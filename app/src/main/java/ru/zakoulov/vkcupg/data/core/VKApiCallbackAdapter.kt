// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

import com.vk.api.sdk.VKApiCallback

/**
 * The adapter that transform [VKApiCallback] to [CommonResponseCallback]
 */
class VKApiCallbackAdapter<VK, R, M: Mapper<VK, R>>(
    private val commonResponseCallback: CommonResponseCallback<R>,
    private val errorMessage: String,
    private val mapper: M
) : VKApiCallback<VK> {
    override fun success(result: VK) {
        commonResponseCallback.success(mapper.map(result))
    }

    override fun fail(error: Exception) {
        commonResponseCallback.fail(error.localizedMessage ?: error.message ?: errorMessage)
    }
}
