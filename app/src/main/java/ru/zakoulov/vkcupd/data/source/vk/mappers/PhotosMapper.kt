package ru.zakoulov.vkcupd.data.source.vk.mappers

import ru.zakoulov.vkcupd.data.core.Mapper
import ru.zakoulov.vkcupd.data.models.Photo
import ru.zakoulov.vkcupd.data.models.Photos
import ru.zakoulov.vkcupd.data.source.vk.models.VkPhotos

class PhotosMapper : Mapper<VkPhotos, Photos> {
    override fun map(input: VkPhotos): Photos {
        return Photos(
            count = input.count,
            photos = input.items.map {
                Photo(
                    id = it.id,
                    src = it.sizes.find { it.type == "q" }?.url ?:
                    it.sizes.find { it.type == "p" }?.url ?:
                    it.sizes.find { it.type == "x" }?.url ?:
                    it.sizes.find { it.type == "m" }?.url ?:
                    it.sizes.find { it.type == "s" }?.url!!
                )
            }
        )
    }
}
