package ru.zakoulov.vkcupf.data.source

sealed class GroupStatus {
    class Success : GroupStatus()
    class Loading : GroupStatus()
    class Fail(val message: String) : GroupStatus()
}