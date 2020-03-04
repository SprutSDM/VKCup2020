package ru.zakoulov.vkcupf.data

sealed class RequestStatus<T>(val data: T?) {
    class Success<T>(data: T?) : RequestStatus<T>(data)
    class Loading<T>(data: T?) : RequestStatus<T>(data)
    class Fail<T>(data: T?, val message: String, var viewed: Boolean = false) : RequestStatus<T>(data)
}
