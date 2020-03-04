// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

/**
 * A common callback for getting response from requests in data sources
 */
interface CommonResponseCallback<T> {
    fun success(response: T)
    fun fail(failMessage: String)
}
