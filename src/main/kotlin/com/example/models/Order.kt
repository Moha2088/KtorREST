package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(val number:String, val contents:List<OrderItem>)

val orderStorage = listOf(
    Order("28-02-2024", listOf(
        OrderItem("Sandwich", 6, 20.00),
        OrderItem("MeatBall Sandwich", 4, 28.50),
        OrderItem("Triple CheeseBurger", 7, 30.00),
        OrderItem("Krusty Burger", 10, 25.00)
    )),

    Order("29-02-2024", listOf(
        OrderItem("Hot Wings", 12, 10.00),
        OrderItem("Hot Dogs", 8, 16.00),
        OrderItem("Grilled Cheese", 2, 10.00)
    ))
)

@Serializable
data class OrderItem(val item:String, val amount:Int, val price:Double)