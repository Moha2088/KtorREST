package com.example.models

import kotlinx.serialization.Serializable
import java.security.MessageDigest

@Serializable
data class User(val userName: String?,
                var password:String?,
                var token:String? = null){

    init {
        val hashValue =this.password!!.toByteArray()
        val sha256 = MessageDigest.getInstance("SHA-256")
        sha256.update(hashValue)
        val passwordHash = sha256.digest(this.password!!.toByteArray())
        this.password = passwordHash.toString()
    }
}
