package ru.zakoulov.vkcupd.data

import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.core.RequestStatus
import ru.zakoulov.vkcupd.data.core.SparseStorage
import ru.zakoulov.vkcupd.data.core.SparseStorageCallback
import ru.zakoulov.vkcupd.data.core.StatusLiveData
import ru.zakoulov.vkcupd.data.models.Album
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.models.Photos
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

    private val photos = SparseStorage(object : SparseStorageCallback<Photos> {
        override fun initData(key: Int) = Photos(-1, emptyList())

        override fun fetchData(key: Int, data: StatusLiveData<Photos>, size: Int, quiet: Boolean) {
            if (data.isLoading()) {
                return
            }
            // Already have all photos, don't DDoS VK
            data.data.let {
                if (it.count == it.photos.size) {
                    return
                }
            }
            data.setLoading(quiet)
            remoteSource.getPhotos(key, NUM_OF_PHOTOS_FOR_FETCHING, size, object : CommonResponseCallback<Photos> {
                override fun success(response: Photos) {
                    data.value = RequestStatus.Success(
                        Photos(
                            count = response.count,
                            photos = data.data.photos + response.photos
                    ))
                }

                override fun fail(failMessage: String) {
                    data.setFail(failMessage)
                }
            })
        }
    })

    fun getPhotos(albumId: Int) = photos[albumId]

    fun fetchNewPhotos(albumId: Int, quiet: Boolean = false) {
        photos.fetchNewData(albumId, quiet)
    }

    companion object {
        const val NUM_OF_ALBUMS_FOR_FETCHING = 20
        const val NUM_OF_PHOTOS_FOR_FETCHING = 40
    }
}
