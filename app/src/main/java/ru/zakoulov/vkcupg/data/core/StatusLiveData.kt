// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

import androidx.lifecycle.MutableLiveData

/**
 * The LiveData with [RequestStatus]
 */
class StatusLiveData<T>(value: RequestStatus<T>) : MutableLiveData<RequestStatus<T>>(value) {

    val data: T
        get() = value?.data!!

    fun isFailed() = this is RequestStatus.Fail<*>
    fun isSuccessed() = this is RequestStatus.Success<*>
    fun isLoading() = this is RequestStatus.Loading<*>

    fun setLoading() {
        value = RequestStatus.Loading(data)
    }
}
