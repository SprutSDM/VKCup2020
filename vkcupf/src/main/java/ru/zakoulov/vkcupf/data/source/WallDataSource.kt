package ru.zakoulov.vkcupf.data.source

interface WallDataSource {

    fun getDateOfFirstPost(groupId: Int, callback: CommonResponseCallback<Long>)
}
