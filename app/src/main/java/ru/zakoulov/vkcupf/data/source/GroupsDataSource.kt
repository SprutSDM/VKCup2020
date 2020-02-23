package ru.zakoulov.vkcupf.data.source

import ru.zakoulov.vkcupf.data.Group

interface GroupsDataSource {

    fun getAllGroups(callback: CommonResponseCallback<List<Group>>)

    fun getGroupMembers(groupId: Int, onlyFriends: Boolean, callback: CommonResponseCallback<Int>)
}
