// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupf.data.source

/**
 * A class that allows to wait for the result of several requests.
 */
abstract class ComplexFetchController<R>(
    private var callback: CommonResponseCallback<R>?
) {

    protected abstract val fetchingDataList: List<FetchingData<out Any>>

    abstract fun fetch()

    private fun isCompletelyFetched() = fetchingDataList.count { it.isFetched() } == fetchingDataList.size

    fun fetchedNewData() {
        if (isCompletelyFetched()) {
            callback?.success(makeResult())
            callback = null
        }
    }

    fun failNewData(failMessage: String) {
        callback?.fail(failMessage)
        callback = null
    }

    protected inner class FetchResponseCallback<F>(
        private val fetchingData: FetchingData<F>
    ) : CommonResponseCallback<F> {

        override fun success(response: F) {
            fetchingData.data = response
            fetchedNewData()
        }

        override fun fail(failMessage: String) {
            failNewData(failMessage)
        }
    }

    abstract fun makeResult(): R

    protected class FetchingData<T> {
        var data: T? = null

        fun isFetched() = data != null
    }
}
