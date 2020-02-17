package ru.zakoulov.vkcupa.data

interface Mapper<I, O> {
    fun map(input: I): O
}
