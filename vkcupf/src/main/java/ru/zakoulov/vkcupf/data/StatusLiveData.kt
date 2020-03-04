// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupf.data

import androidx.lifecycle.MutableLiveData

class StatusLiveData<T>(value: RequestStatus<T>? = null) : MutableLiveData<RequestStatus<T>>(value) {

    val data: T?
        get() = value?.data
}
