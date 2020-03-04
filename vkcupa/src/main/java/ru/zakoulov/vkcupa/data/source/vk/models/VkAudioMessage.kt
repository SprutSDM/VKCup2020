package ru.zakoulov.vkcupa.data.source.vk.models

data class VkAudioMessage(
    val duration: Int,
    val waveform: List<Int>,
    val linkOgg: String,
    val linkMp3: String
)
