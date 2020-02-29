package ru.zakoulov.vkcupd.data

import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.core.RequestStatus
import ru.zakoulov.vkcupd.data.core.StatusLiveData
import ru.zakoulov.vkcupd.data.models.Album
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.source.AlbumsDataSource

class AlbumsRepository(
    private val remoteSource: AlbumsDataSource
) {
    var totalCountOfAlbums = -1

    var albums: StatusLiveData<List<Album>> = StatusLiveData(RequestStatus.Empty(emptyList()))

    fun fetchNewAlbums(quiet: Boolean = false) {
        if (albums.isLoading()) {
            return
        }

        // Already have all albums, don't DDoS VK
        if (totalCountOfAlbums == albums.data.size) {
            return
        }

        albums.setLoading(quiet)
        remoteSource.getAlbums(NUM_OF_ALBUMS_FOR_FETCHING, albums.data.size, object : CommonResponseCallback<Albums> {
            override fun success(response: Albums) {
                totalCountOfAlbums = response.count
                albums.value = RequestStatus.Success(albums.data + response.albums)
            }

            override fun fail(failMessage: String) {
                albums.setFail(failMessage)
            }
        })
    }

    companion object {
        const val NUM_OF_ALBUMS_FOR_FETCHING = 20
    }
}
