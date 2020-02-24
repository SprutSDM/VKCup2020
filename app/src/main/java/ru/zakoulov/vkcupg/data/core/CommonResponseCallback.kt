package ru.zakoulov.vkcupg.data.core

interface CommonResponseCallback<T> {
    fun success(response: T)
    fun fail(failMessage: String)
}
