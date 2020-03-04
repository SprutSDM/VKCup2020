// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupd.data.core

/**
 * The enumeration of possible states of request
 */
sealed class RequestStatus<T>(val data: T) {
    class Success<T>(data: T) : RequestStatus<T>(data)
    class Loading<T>(data: T, val quiet: Boolean = false) : RequestStatus<T>(data)
    class Fail<T>(data: T, val message: String, var viewed: Boolean = false) : RequestStatus<T>(data)
    // Only for first state, when we just don't have data. Escaping nullability
    class Empty<T>(data: T) : RequestStatus<T>(data)

    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
    fun isFail() = this is Fail
    fun isEmpty() = this is Empty
}
