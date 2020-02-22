package ru.zakoulov.vkcupf.data.source.vk.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkGetGroupMembersRequest(
    groupId: Int,
    onlyFriends: Boolean = false
) : VKRequest<Int>("groups.getMembers") {

    init {
        addParam("group_id", groupId)
        addParam("count", 0)
        if (onlyFriends) {
            addParam("fields", "friends")
        }
    }

    override fun parse(r: JSONObject): Int {
        return r.getJSONObject("response").getInt("count")
    }
}
