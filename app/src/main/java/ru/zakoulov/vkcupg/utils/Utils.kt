package ru.zakoulov.vkcupf.utils

fun Int.toShortPretty(): String {
    return when {
        this >= 1_000_000 -> "${this / 1_000_000},${(this / 100_000) % 10}M"
        this >= 1_000 -> "${this / 1_000},${(this / 100) % 10}K"
        else -> toString()
    }
}
