package com.example.Repositories

import com.example.models.Customer
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson

class CustomerRepository {
    private val client: MongoClient =
        MongoClient.create("mongodb+srv://maxamed14:V8AV4PCGop0TFMt8@cluster0.dd89cjv.mongodb.net/?retryWrites=true&w=majority")
    private val database: MongoDatabase = client.getDatabase("UCL")
    private val collection: MongoCollection<Customer> = database.getCollection<Customer>("Customer")


    suspend fun addCustomer(customer: Customer) {
        collection.insertOne(customer)
    }

    suspend fun getCustomer(id:String) :MutableList<Customer> {
        val filter = Filters.eq(Customer::id.name,id)
        val customer = mutableListOf<Customer>()

        collection.find(filter).collect{
            customer.add(it)
        }

        return customer
    }

    suspend fun getAll() : MutableList<Customer>{
        val filter = Filters.empty()
        val namesList = mutableListOf<Customer>()

        collection.find(filter).collect{
            namesList.add(it)
        }

        return namesList
    }

    suspend fun deleteCustomer(id:String) {
        val filter = Filters.eq(Customer::id.name, id)
        collection.deleteOne(filter)

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