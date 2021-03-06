// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupd.data.core

import androidx.lifecycle.MutableLiveData

/**
 * The LiveData with [RequestStatus]
 */
class StatusLiveData<T>(value: RequestStatus<T>) : MutableLiveData<RequestStatus<T>>(value) {

    val data: T
        get() = value!!.data

    fun isFailed() = value!!.isFail()
    fun isSucceed() = value!!.isSuccess()
    fun isLoading() = value!!.isLoading()
    fun isEmpty() = value!!.isEmpty()

    fun setLoading(quiet: Boolean = false) {
        value = RequestStatus.Loading(data, quiet)
    }

    fun setFail(message: String) {
        value = RequestStatus.Fail(data, message)
    }
}
