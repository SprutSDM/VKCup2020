package ru.zakoulov.vkcupg.data.source.vk.mappers

import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.models.Market
import ru.zakoulov.vkcupg.data.models.Markets
import ru.zakoulov.vkcupg.data.source.vk.models.VkMarkets

class MarketsMapper : Mapper<VkMarkets, Markets> {
    override fun map(input: VkMarkets): Markets {
        return Markets(
            count = input.count,
            markets = input.items.map {
                Market(
                    id = it.id,
                    title = it.name,
                    photo = it.photo,
                    isClosed = it.isClosed == 1,
                    isMember = it.isMember == 1
                )
            }
        )
    }
}
