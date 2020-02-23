package ru.zakoulov.vkcupf.data

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.WallDataSource

class WallRepository(
    private val remoteSource: WallDataSource
) {
    private val cacheDateOfFirstPost: SparseArray<Long> = SparseArray()

    fun getDateOfFirstPost(groupId: Int, callback: CommonResponseCallback<Long>) {
        if (cacheDateOfFirstPost.contains(groupId)) {
            callback.success(cacheDateOfFirstPost.get(groupId))
            return
        }
        remoteSource.getDateOfFirstPost(groupId, object : CommonResponseCallback<Long> {
            override fun success(response: Long) {
                cacheDateOfFirstPost[groupId] = response
                callback.success(response)
            }

            override fun fail(failMessage: String) {
                callback.fail(failMessage)
            }
        })
    }
}
