// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupf.data.source

import ru.zakoulov.vkcupf.data.RequestStatus
import ru.zakoulov.vkcupf.data.StatusLiveData

class LiveDataResponseCallback<T>(
    private val data: StatusLiveData<T>
) : CommonResponseCallback<T> {
    override fun success(response: T) {
        data.value = RequestStatus.Success(response)
    }

    override fun fail(failMessage: String) {
        data.value = RequestStatus.Fail(data.data, failMessage)
    }
}
