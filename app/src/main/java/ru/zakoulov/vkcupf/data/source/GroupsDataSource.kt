package ru.zakoulov.vkcupf.data.source

import ru.zakoulov.vkcupf.data.Group

interface GroupsDataSource {

    fun getGroups(count: Int, offset: Int, callback: CommonResponseCallback<List<Group>>)

    fun getGroupMembers(groupId: Int, onlyFriends: Boolean, callback: CommonResponseCallback<Int>)
}
