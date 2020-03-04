package ru.zakoulov.vkcupf.data

interface Mapper<I, O> {
    fun map(input: I): O
}
