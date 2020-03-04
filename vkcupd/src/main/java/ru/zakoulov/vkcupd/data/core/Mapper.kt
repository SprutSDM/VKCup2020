// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupd.data.core

/**
 * A basic interface for all mappers
 */
interface Mapper<I, O> {
    fun map(input: I): O
}

object IntMapper : Mapper<Int, Int> {
    override fun map(input: Int) = input
}
