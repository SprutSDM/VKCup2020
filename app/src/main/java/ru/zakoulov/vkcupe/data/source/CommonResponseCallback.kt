package ru.zakoulov.vkcupe.data.source

interface CommonResponseCallback<T> {
    fun success(response: T)
    fun fail(failMessage: String)
}