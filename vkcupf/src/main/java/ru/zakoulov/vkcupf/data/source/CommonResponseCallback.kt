package ru.zakoulov.vkcupf.data.source

interface CommonResponseCallback<T> {
    fun success(response: T)
    fun fail(failMessage: String)
}
