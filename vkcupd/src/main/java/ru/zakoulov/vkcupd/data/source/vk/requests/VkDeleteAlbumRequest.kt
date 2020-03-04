package ru.zakoulov.vkcupd.data.source.vk.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkDeleteAlbumRequest(
    albumId: Int
) : VKRequest<Int>("photos.deleteAlbum") {

    init {
        addParam("album_id", albumId)
    }

    override fun parse(r: JSONObject): Int {
        return r.getInt("response")
    }
}
