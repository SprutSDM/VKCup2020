package ru.zakoulov.vkcupf.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.GroupStatus
import ru.zakoulov.vkcupf.data.source.GroupsDataSource
import ru.zakoulov.vkcupf.data.source.LiveDataResponseCallback
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

    fun getGroupById(groupId: Int): Group? = groups.value?.find { it.id == groupId }

    fun getGroupInfo(group: Group): LiveData<GroupInfo> {
        val groupInfo = MutableLiveData<GroupInfo>()
        remoteGroupSource.getGroupInfo(group, remoteWallSource, LiveDataResponseCallback(groupInfo) {
            // Handle error
        })
        return groupInfo
    }
}
