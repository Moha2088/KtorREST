package com.example.Repositories

import com.example.models.User
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class UserRepository{

    private val client: MongoClient =
        MongoClient.create("mongodb+srv://maxamed14:V8AV4PCGop0TFMt8@cluster0.dd89cjv.mongodb.net/?retryWrites=true&w=majority")

    private val database: MongoDatabase = client.getDatabase("UCL")
    private val collection: MongoCollection<User> = database.getCollection<User>("User")

    suspend fun addUser(user: User){
        collection.insertOne(user)
    }

    suspend fun getAll() : MutableList<User> {
        val filter = Filters.empty()
        val userList = mutableListOf<User>()
        collection.find(filter).collect{
            userList.add(it)
        }

        return userList
    }

    suspend fun getUser(name:String) : MutableList<User> {
        val filter = Filters.eq(User::userName.name)
        val user:MutableList<User> = mutableListOf()
        collection.find(filter).collect{
          user.add(it)
        }

        return user
    }

    suspend fun deleteUser(name:String) : Int {
        val userToDelete = Filters.eq(User::userName.name, name)
        val deleteCount:Int
        collection.deleteOne(userToDelete).also {
            deleteCount = it.deletedCount.toInt()
        }

        return deleteCount
    }
}