package ru.zakoulov.vkcupg.data.source.vk

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vk.api.sdk.VK
import ru.zakoulov.vkcupg.data.core.CommonResponseCallback
import ru.zakoulov.vkcupg.data.core.Mapper
import ru.zakoulov.vkcupg.data.core.VKApiCallbackAdapter
import ru.zakoulov.vkcupg.data.models.Products
import ru.zakoulov.vkcupg.data.source.FavesDataSource
import ru.zakoulov.vkcupg.data.source.vk.models.VkProducts
import ru.zakoulov.vkcupg.data.source.vk.requests.VkAddProductToFaves
import ru.zakoulov.vkcupg.data.source.vk.requests.VkGetProductFaveStatus
import ru.zakoulov.vkcupg.data.source.vk.requests.VkRemoveProductFromFaves

class VkFavesDataSource(
    private val productsMapper: Mapper<VkProducts, Products>
) : FavesDataSource {

    private val gson = Gson()
    private val jsonParser = JsonParser()

    override fun addProduct(marketId: Int, productId: Int, callback: CommonResponseCallback<Int>) {
        VK.execute(VkAddProductToFaves(marketId, productId),
            VKApiCallbackAdapter(callback, "Error add product to faves", object: Mapper<Int, Int> {
                override fun map(input: Int): Int {
                    return input
                }
            }))
    }

    override fun removeProduct(marketId: Int, productId: Int, callback: CommonResponseCallback<Int>) {
        VK.execute(VkRemoveProductFromFaves(marketId, productId),
            VKApiCallbackAdapter(callback, "Error add product to faves", object: Mapper<Int, Int> {
                override fun map(input: Int): Int {
                    return input
                }
            }))
    }

    override fun getProductFaveStatus(marketId: Int, productId: Int, callback: CommonResponseCallback<Products>) {
        VK.execute(VkGetProductFaveStatus(gson, jsonParser, marketId, productId),
            VKApiCallbackAdapter(callback, "Error get product fave status", productsMapper))
    }
}
