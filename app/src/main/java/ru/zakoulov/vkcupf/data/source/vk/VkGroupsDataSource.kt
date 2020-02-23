package ru.zakoulov.vkcupf.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.GroupInfo
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.ComplexFetchController
import ru.zakoulov.vkcupf.data.source.GroupsDataSource
import ru.zakoulov.vkcupf.data.source.WallDataSource
import ru.zakoulov.vkcupf.data.source.vk.mappers.GroupMapper
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroup
import ru.zakoulov.vkcupf.data.source.vk.requests.VkGetGroupMembersRequest
import ru.zakoulov.vkcupf.data.source.vk.requests.VkGetGroupsCommand

class VkGroupsDataSource(private val groupMapper: GroupMapper) : GroupsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getAllGroups(callback: CommonResponseCallback<List<Group>>) {
        VK.execute(VkGetGroupsCommand(gson, jsonParser), object : VKApiCallback<List<VkGroup>> {
            override fun success(result: List<VkGroup>) {
                callback.success(result.map { groupMapper.map(it) })
            }

            override fun fail(error: Exception) {
                callback.fail(error.localizedMessage ?: error.message ?: "Error getting groups")
            }
        })
    }

    override fun getGroupMembers(groupId: Int, onlyFriends: Boolean, callback: CommonResponseCallback<Int>) {
        VK.execute(VkGetGroupMembersRequest(groupId, onlyFriends), object : VKApiCallback<Int> {
            override fun success(result: Int) {
                callback.success(result)
            }

            override fun fail(error: Exception) {
                callback.fail(error.localizedMessage ?: error.message ?: "Error getting group members.")
            }
        })
    }

    override fun getGroupInfo(
        group: Group,
        wallDataSource: WallDataSource,
        callback: CommonResponseCallback<GroupInfo>
    ) {
        val groupInfoFetchController = GroupInfoFetchController(group, this, wallDataSource, callback)
        groupInfoFetchController.fetch()
    }

    private class GroupInfoFetchController(
        private val group: Group,
        private val remoteGroupSource: GroupsDataSource,
        private val remoteWallSource: WallDataSource,
        callback: CommonResponseCallback<GroupInfo>
    ) : ComplexFetchController<GroupInfo>(callback){

        private var members = FetchingData<Int>()
        private var friends = FetchingData<Int>()
        private var lastPostDate = FetchingData<Long>()

        override val fetchingDataList = listOf(members, friends, lastPostDate)

        override fun fetch() {
            remoteGroupSource.getGroupMembers(group.id, false, FetchResponseCallback(members))
            remoteGroupSource.getGroupMembers(group.id, true, FetchResponseCallback(friends))
            remoteWallSource.getDateOfFirstPost(group.id, FetchResponseCallback(lastPostDate))
        }

        override fun makeResult() = GroupInfo(
            id = group.id,
            title = group.title,
            description = group.description,
            membersInGroup = members.data!!,
            friendsInGroup = friends.data!!,
            lastPostDate = lastPostDate.data!!)
    }
}
