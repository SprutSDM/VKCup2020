package ru.zakoulov.vkcupf.data.source.vk.mappers

import ru.zakoulov.vkcupf.data.Group
import ru.zakoulov.vkcupf.data.Mapper
import ru.zakoulov.vkcupf.data.source.vk.models.VkGroup

class GroupMapper : Mapper<VkGroup, Group> {

    override fun map(input: VkGroup): Group {
        return Group(
            id = input.id,
            title = input.title,
            description = input.description,
            img = input.img,
            members = input.membersCount
        )
    }
}
