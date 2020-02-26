package ru.zakoulov.vkcupg.data.source.vk.mappers

import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.models.Product
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts

class ProductsMapper : Mapper<VkProducts, Products> {
    override fun map(input: VkProducts): Products {
        return Products(
            count = input.count,
            products = input.items.map {
                Product(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    priceText = it.price.text,
                    photo = it.photo
                )
            }
        )
    }
}
