// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

/**
 * The LiveData wrapper over [CommonResponseCallback]
 */
class ReplaceLiveDataResponseCallback<T>(
    private val data: StatusLiveData<T>
) : CommonResponseCallback<T> {
    override fun success(response: T) {
        data.value = RequestStatus.Success(response)
    }

    override fun fail(failMessage: String) {
        data.value = RequestStatus.Fail(data.data, failMessage)
    }
}
