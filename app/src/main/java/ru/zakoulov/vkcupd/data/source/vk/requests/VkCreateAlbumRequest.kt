package ru.zakoulov.vkcupd.data.source.vk.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkCreateAlbumRequest(
    albumTitle: String
) : VKRequest<Int>("photos.createAlbum") {

    init {
        addParam("title", albumTitle)
    }

    override fun parse(r: JSONObject): Int {
        return r.getJSONObject("response").getInt(ALBUM_ID)
    }

    companion object {
        const val ALBUM_ID = "id"
    }
}
