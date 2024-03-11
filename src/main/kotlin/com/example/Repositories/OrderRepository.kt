package com.example.Repositories

import com.example.models.Order
import com.example.models.OrderItem
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.example.config

class OrderRepository {

    private val connectionString = config.property("Mongo.connectionString").getString()

    private val client: MongoClient =
        MongoClient.create(connectionString)
    private val database: MongoDatabase = client.getDatabase("UCL")
    private val collection: MongoCollection<Order> = database.getCollection<Order>("Order")


    suspend fun addOrderItem(order: Order) {
        collection.insertOne(order)
    }

    suspend fun getOrder(number:String) : MutableList<Order> {
        val order = mutableListOf<Order>()
        val filter = Filters.eq(Order::number.name, number)
        collection.find(filter).collect{
            order.add(it)
        }

        return order
    }

    suspend fun getAll(): MutableList<Order> {
        val orders = mutableListOf<Order>()
        val filter = Filters.empty()

        collection.find(filter).collect {
            orders.add(it)
        }

        return orders
    }

    suspend fun updateOrder(number:String, order:List<OrderItem>){
        val filter = Filters.eq(Order::number.name, number)
        val update = Updates.set(Order::contents.name, order)
        collection.updateOne(filter, update)
    }

    suspend fun deleteOrder(number:String) : Int {
        val filter = Filters.eq(Order::number.name, number)
        val deletedCount:Int
        collection.deleteOne(filter).also {
            deletedCount = it.deletedCount.toInt()
        }

        return deletedCount
    }

    suspend fun deleteAll() : Int {
        val filter = Filters.empty()
        val deletedCount:Long

        collection.deleteMany(filter).also {
            deletedCount = it.deletedCount
        }

        return deletedCount.toInt()
    }
}