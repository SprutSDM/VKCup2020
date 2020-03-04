package ru.zakoulov.vkcupg.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.core.VKApiCallbackAdapter
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.ProductsDataSource
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts
import ru.zakoulov.vkcupg.data.source.vk.requests.VkGetProductsRequest

class VkProductsDataSource(
    private val productsMapper: Mapper<VkProducts, Products>
) : ProductsDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun fetchProducts(marketId: Int, count: Int, offset: Int, callback: CommonResponseCallback<Products>) {
        VK.execute(VkGetProductsRequest(gson, jsonParser, marketId, count, offset),
            VKApiCallbackAdapter(callback, "Error fetching products", productsMapper))
    }
}
