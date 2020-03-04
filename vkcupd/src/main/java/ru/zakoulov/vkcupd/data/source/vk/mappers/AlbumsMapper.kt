package ru.zakoulov.vkcupd.data.source.vk.mappers

import ru.zakoulov.vkcupd.data.core.Mapper
import ru.zakoulov.vkcupd.data.models.Album
import ru.zakoulov.vkcupd.data.models.Albums
import ru.zakoulov.vkcupd.data.source.vk.models.VkAlbums

class AlbumsMapper : Mapper<VkAlbums, Albums> {
    override fun map(input: VkAlbums): Albums {
        return Albums(
            count = input.count,
            albums = input.items.map {
                Album(
                    id = it.id,
                    title = it.title,
                    preview = it.sizes.find { it.type == "r" }?.src ?:
                            it.sizes.find { it.type == "x" }?.src ?:
                            it.sizes.find { it.type == "m" }?.src ?:
                            it.sizes.find { it.type == "s" }?.src!!,
                    size = it.size
                )
            }
        )
    }
}
