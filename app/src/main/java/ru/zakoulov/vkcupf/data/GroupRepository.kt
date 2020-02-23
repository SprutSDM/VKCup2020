package ru.zakoulov.vkcupf.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.GroupStatus
import ru.zakoulov.vkcupf.data.source.GroupsDataSource
import ru.zakoulov.vkcupf.data.source.WallDataSource

class GroupRepository(
    private val remoteGroupSource: GroupsDataSource,
    private val remoteWallSource: WallDataSource
) {

    private val groups = MutableLiveData<List<Group>>()
    fun getGroups(): LiveData<List<Group>> = groups

    private val groupStatus = MutableLiveData<GroupStatus>()
    fun getGroupStatus(): LiveData<GroupStatus> = groupStatus

    fun getAllGroups() {
        groupStatus.value = GroupStatus.Loading()
        remoteGroupSource.getAllGroups(object : CommonResponseCallback<List<Group>> {
            override fun success(response: List<Group>) {
                groupStatus.value = GroupStatus.Success()
                groups.value = response.reversed()
            }

            override fun fail(failMessage: String) {
                groupStatus.value = GroupStatus.Fail(failMessage)
            }
        })
    }

    fun getGroupInfo(group: Group): LiveData<GroupInfo> {
        val groupInfo = MutableLiveData<GroupInfo>()
        val groupInfoFetchController = GroupInfoFetchController(group, groupInfo, remoteGroupSource, remoteWallSource)
        groupInfoFetchController.fetchInfo()
        return groupInfo
    }

    private class GroupInfoFetchController(
        private val group: Group,
        private val groupInfo: MutableLiveData<GroupInfo>,
        private val remoteGroupSource: GroupsDataSource,
        private val remoteWallSource: WallDataSource) {

        private var members: Int = -1
        private var friends: Int = -1
        private var lastPostDate = -1L

        fun fetchInfo() {
            remoteGroupSource.getGroupMembers(group.id, false, object : CommonResponseCallback<Int> {
                override fun success(response: Int) {
                    members = response
                    if (isFullyFetched()) {
                        updateGroupInfo()
                    }
                }
                override fun fail(failMessage: String) { }
            })
            remoteGroupSource.getGroupMembers(group.id, true, object : CommonResponseCallback<Int> {
                override fun success(response: Int) {
                    friends = response
                    if (isFullyFetched()) {
                        updateGroupInfo()
                    }
                }
                override fun fail(failMessage: String) { }
            })
            remoteWallSource.getDateOfFirstPost(group.id, object: CommonResponseCallback<Long> {
                override fun success(response: Long) {
                    lastPostDate = response
                    if (isFullyFetched()) {
                        updateGroupInfo()
                    }
                }
                override fun fail(failMessage: String) { }
            })
        }

        fun isFullyFetched(): Boolean = members != -1 && friends != -1 && lastPostDate != -1L

        fun updateGroupInfo() {
            groupInfo.value = GroupInfo(
                id = group.id,
                title = group.title,
                description = group.description,
                membersInGroup = members,
                friendsInGroup = friends,
                lastPostDate = lastPostDate)
        }
    }
}
