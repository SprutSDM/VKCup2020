package ru.zakoulov.vkcupg.data.models

data class Product(
    val title: String,
    val description: String,
    val priceText: String,
    val photo: String
)

data class Products(
    val count: Int,
    val products: List<Product>
)
