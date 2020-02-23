// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupf.data.source

interface ComplexFetchCallback {
    fun fetchedNewData()
    fun failNewData(failMessage: String)
}
