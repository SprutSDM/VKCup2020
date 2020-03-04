package ru.zakoulov.vkcupe.data

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupe.data.source.CommonResponseCallback
import ru.zakoulov.vkcupe.data.source.PostStatus
import ru.zakoulov.vkcupe.data.source.PostsDataSource

class PostsRepository(
    private val remoteSource: PostsDataSource
) {

    val postStatus = MutableLiveData<PostStatus>()

    fun addPost(message: String?, photos: List<Uri>) {
        postStatus.value = PostStatus.Loading()
        remoteSource.addPost(message, photos, object : CommonResponseCallback<Int> {
            override fun success(response: Int) {
                postStatus.value = PostStatus.Success(response)
            }

            override fun fail(failMessage: String) {
                postStatus.value = PostStatus.Fail(failMessage)
            }
        })
    }
}
