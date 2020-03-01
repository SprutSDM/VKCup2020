// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupd.data.core

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import androidx.core.util.size

/**
 * The class that manages fetching new data and their status
 */
class SparseStorage<D>(
    private val callback: SparseStorageCallback<D>
) {
    private val data: SparseArray<StatusLiveData<D>> = SparseArray()

    operator fun get(key: Int): StatusLiveData<D> {
        if (key in data) {
            data[key].let {
                if (it.isFailed()) {
                    callback.fetchData(key, it)
                }
                return it
            }
        }
        val newData = StatusLiveData(RequestStatus.Empty(callback.initData(key)))
        data[key] = newData
        callback.fetchData(key, newData)
        return newData
    }

    fun fetchNewData(key: Int, quiet: Boolean = false) {
        if (key !in data) {
            get(key)
        } else {
            callback.fetchData(key, data[key], quiet)
        }
    }
}
