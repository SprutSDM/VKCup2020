package ru.zakoulov.vkcupg.data.models

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val priceText: String,
    val photo: String,
    val isFavorite: Boolean
)

data class Products(
    val count: Int,
    val products: List<Product>
)
