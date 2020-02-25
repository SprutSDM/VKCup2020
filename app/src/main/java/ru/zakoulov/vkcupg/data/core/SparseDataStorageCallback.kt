// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

/**
 * A callback for [SparseDataStorage]
 */
interface SparseDataStorageCallback<D> {
    fun initData(key: Int): D

    fun fetchData(key: Int, data: StatusLiveData<D>)
}
