package ru.zakoulov.vkcupd.data.source

import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.models.Albums

interface AlbumsDataSource {

    fun getAlbums(count: Int, offset: Int, callback: CommonResponseCallback<Albums>)
}
