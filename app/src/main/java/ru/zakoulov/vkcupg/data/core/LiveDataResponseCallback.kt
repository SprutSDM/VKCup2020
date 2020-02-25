// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

/**
 * The LiveData wrapper over [CommonResponseCallback]
 */
class LiveDataResponseCallback<T>(
    private val data: StatusLiveData<T>,
    private val onSuccess: (T) -> Unit = {}
) : CommonResponseCallback<T> {
    override fun success(response: T) {
        onSuccess(response)
        data.value = RequestStatus.Success(response)
    }

    override fun fail(failMessage: String) {
        data.value = RequestStatus.Fail(data.data, failMessage)
    }
}
