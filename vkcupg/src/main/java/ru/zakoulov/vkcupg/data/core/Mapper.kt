// © Zakoulov Ilya <zakoylov@gmail.com>
package ru.zakoulov.vkcupg.data.core

/**
 * A basic interface for all mappers
 */
interface Mapper<I, O> {
    fun map(input: I): O
}
