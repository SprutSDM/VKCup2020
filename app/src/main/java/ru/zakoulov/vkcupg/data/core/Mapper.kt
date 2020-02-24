package ru.zakoulov.vkcupg.data.core

interface Mapper<I, O> {
    fun map(input: I): O
}
