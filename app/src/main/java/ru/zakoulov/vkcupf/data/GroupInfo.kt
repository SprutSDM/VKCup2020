package ru.zakoulov.vkcupf.data

data class GroupInfo(
    val id: Int,
    val title: String,
    val description: String,
    val membersInGroup: Int,
    val friendsInGroup: Int,
    val lastPostDate: Long
)
