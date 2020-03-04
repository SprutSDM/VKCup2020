package ru.zakoulov.vkcupe.data.source

import android.net.Uri

interface PostsDataSource {

    fun addPost(message: String?, photos: List<Uri>, callback: CommonResponseCallback<Int>)
}
