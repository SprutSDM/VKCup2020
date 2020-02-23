package ru.zakoulov.vkcupf.data

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import ru.zakoulov.vkcupf.data.source.CommonResponseCallback
import ru.zakoulov.vkcupf.data.source.GroupsDataSource
import ru.zakoulov.vkcupf.data.source.LiveDataResponseCallback
import ru.zakoulov.vkcupf.data.source.WallDataSource

class GroupRepository(
    private val remoteGroupSource: GroupsDataSource,
    private val remoteWallSource: WallDataSource
) {

    private val groups = StatusLiveData<List<Group>>(null)
    fun getGroups(): StatusLiveData<List<Group>> = groups

    private val groupsInfo = SparseArray<StatusLiveData<GroupInfo>>()

    fun getAllGroups() {
        groups.value = RequestStatus.Loading(groups.value?.data)
        remoteGroupSource.getAllGroups(object : CommonResponseCallback<List<Group>> {
            override fun success(response: List<Group>) {
                groups.value = RequestStatus.Success(response.reversed())
            }

            override fun fail(failMessage: String) {
                groups.value = RequestStatus.Fail(groups.value?.data, failMessage)
            }
        })
    }

    fun getGroupById(groupId: Int): Group? = groups.value?.data?.find { it.id == groupId }

    fun getGroupInfo(group: Group): StatusLiveData<GroupInfo> {
        if (group.id in groupsInfo) {
            if (groupsInfo[group.id].value is RequestStatus.Fail) {
                groupsInfo[group.id].value = RequestStatus.Loading(groupsInfo[group.id].data)
                remoteGroupSource.getGroupInfo(group, remoteWallSource, LiveDataResponseCallback(groupsInfo[group.id]))
            }
            return groupsInfo[group.id]
        }
        val groupInfo: StatusLiveData<GroupInfo> = StatusLiveData(RequestStatus.Loading<GroupInfo>(null))
        groupsInfo[group.id] = groupInfo
        remoteGroupSource.getGroupInfo(group, remoteWallSource, LiveDataResponseCallback(groupInfo))
        return groupInfo
    }
}
