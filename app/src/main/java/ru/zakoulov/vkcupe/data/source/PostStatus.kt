package ru.zakoulov.vkcupe.data.source

sealed class PostStatus {
    class Success(val response: Int): PostStatus()
    class Loading: PostStatus()
    class Fail(val message: String): PostStatus()
}
