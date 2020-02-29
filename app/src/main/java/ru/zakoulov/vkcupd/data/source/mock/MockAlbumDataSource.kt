package ru.zakoulov.vkcupd.data.source.mock

import ru.zakoulov.vkcupd.data.core.CommonResponseCallback
import ru.zakoulov.vkcupd.data.models.Album
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.source.AlbumsDataSource

class MockAlbumDataSource : AlbumsDataSource {

    val data = Albums(
        count = 5,
        albums = listOf(
            Album(
                id = -6,
                title = "Фотографии со страницы Виктории",
                preview = "https://sun9-62.userapi.com/c852136/v852136294/1f5783/SXyJDUfavy4.jpg",
                size = 18
            ),
            Album(
                id = -7,
                title = "Фотографии на стене Виктории",
                preview = "https://sun9-47.userapi.com/c824701/v824701865/1923ea/O5gH5Hndvz8.jpg",
                size = 9
            ),
            Album(
                id = -15,
                title = "Сохранённые фотографии Виктории",
                preview = "https://sun9-58.userapi.com/c543107/v543107788/6a20e/yop2bEC4vl8.jpg",
                size = 294
            ),
            Album(
                id = -9000,
                title = "Фотографии с Викторией",
                preview = "https://sun9-72.userapi.com/c830109/v830109209/126539/3UZyfBa5o6A.jpg",
                size = 13
            ),
            Album(
                id = 149464901,
                title = "moments ",
                preview = "https://sun1-94.userapi.com/ydAYpL9ccfNLf09Jv-ZnBy-Bn8Z2WXomAImuXg/A6ZSQ97cyW8.jpg",
                size = 3
            )
        )
    )

    override fun getAlbums(count: Int, offset: Int, callback: CommonResponseCallback<Albums>) {
        callback.success(data)
    }
}
