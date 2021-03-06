package ru.zakoulov.vkcupf.data.source

import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.GroupInfo

interface GroupsDataSource {

    fun getAllGroups(callback: CommonResponseCallback<List<Group>>)

    fun getGroupMembers(groupId: Int, onlyFriends: Boolean, callback: CommonResponseCallback<Int>)

    fun getGroupInfo(group: Group, wallDataSource: WallDataSource, callback: CommonResponseCallback<GroupInfo>)

    fun leaveGroups(groupsId: List<Int>, callback: CommonResponseCallback<List<Int>>)
}
