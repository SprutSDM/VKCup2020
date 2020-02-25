// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set

/**
 * The class that manages fetching new data and their status
 */
class SparseDataStorage<D>(
    private val callback: SparseDataStorageCallback<D>
) {
    private val data: SparseArray<StatusLiveData<D>> = SparseArray()

    operator fun get(key: Int): StatusLiveData<D> {
        if (key in data) {
            data[key].let {
                if (it.isFailed()) {
                    it.setLoading()
                    callback.fetchData(key, it)
                }
                return it
            }
        }
        val newData = StatusLiveData(RequestStatus.Loading(callback.initData(key)))
        data[key] = newData
        callback.fetchData(key, newData)
        return newData
    }
}
