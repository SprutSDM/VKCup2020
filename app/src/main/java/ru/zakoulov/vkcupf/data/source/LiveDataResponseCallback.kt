// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupf.data.source

import androidx.lifecycle.MutableLiveData

class LiveDataResponseCallback<T>(
    private val data: MutableLiveData<T>,
    private val onFail: (failMessage: String) -> Unit
) : CommonResponseCallback<T> {
    override fun success(response: T) {
        data.value = response
    }

    override fun fail(failMessage: String) {
        onFail(failMessage)
    }
}
