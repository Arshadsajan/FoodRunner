package com.mysite.foodrunner.model

data class OrderHistory(
    val restaurantName: String,
    val totalCost: String,
    val orderDate: String,
    val foodItem: List<String>
)
