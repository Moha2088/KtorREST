package com.example.Repositories

import com.example.models.User
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.example.config

class UserRepository {

    private val connectionString = config.property("Mongo.connectionString").getString()
    private val client: MongoClient = MongoClient.create(connectionString)
    private val database: MongoDatabase = client.getDatabase("UCL")
    private val collection: MongoCollection<User> = database.getCollection<User>("User")

    suspend fun addUser(user: User) {
        collection.insertOne(user)
    }

    suspend fun getAll(): MutableList<User> {
        val filter = Filters.empty()
        val userList = mutableListOf<User>()
        collection.find(filter).collect {
            userList.add(it)
        }

        return userList
    }

    suspend fun getUser(uuid: String): MutableList<User> {
        val filter = Filters.eq(User::id.name, uuid)
        val user: MutableList<User> = mutableListOf()
        collection.find(filter).collect {
            user.add(it)
        }

        return user
    }

    suspend fun deleteUser(uuid: String): Int {
        val userToDelete = Filters.eq(User::id.name, uuid)
        val deleteCount: Int
        collection.deleteOne(userToDelete).also {
            deleteCount = it.deletedCount.toInt()
        }

        return deleteCount
    }

    suspend fun deleteAll(): Int {
        val deletedCount: Int
        val filter = Filters.empty()
        collection.deleteMany(filter).also {
            deletedCount = it.deletedCount.toInt()
        }

        return deletedCount
    }
}