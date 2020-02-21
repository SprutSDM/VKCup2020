package ru.zakoulov.vkcupe.data

import android.net.Uri
import ru.zakoulov.vkcupe.data.source.CommonResponseCallback
import ru.zakoulov.vkcupe.data.source.PostsDataSource

class PostsRepository(
    private val remoteSource: PostsDataSource
) : PostsDataSource {

    override fun addPost(message: String?, photos: List<Uri>, callback: CommonResponseCallback<Int>) {
        remoteSource.addPost(message, photos, callback)
    }
}
