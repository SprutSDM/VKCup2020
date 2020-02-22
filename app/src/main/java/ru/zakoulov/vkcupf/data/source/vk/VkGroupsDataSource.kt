package ru.zakoulov.vkcupf.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.GroupsDataSource
import ru.zakoulov.vkcupf.data.source.vk.mappers.GroupMapper
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroups
import ru.zakoulov.vkcupf.data.source.vk.requests.VkGetGroupMembersRequest
import ru.zakoulov.vkcupf.data.source.vk.requests.VkGetGroupsRequest

class VkGroupsDataSource(private val groupMapper: GroupMapper) : GroupsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun getGroups(count: Int, offset: Int, callback: CommonResponseCallback<List<Group>>) {
        VK.execute(VkGetGroupsRequest(gson, jsonParser, count, offset), object : VKApiCallback<VkGroups> {
            override fun success(result: VkGroups) {
                callback.success(result.items.map { groupMapper.map(it)})
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
}
