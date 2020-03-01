// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupd.data.core

/**
 * A callback for [SparseStorage]
 */
interface SparseStorageCallback<D> {
    fun initData(key: Int): D

    fun fetchData(key: Int, data: StatusLiveData<D>, quiet: Boolean = false, removePrevious: Boolean = false)
}
