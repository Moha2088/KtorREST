package com.example.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.util.UUID

@Serializable
data class User(
    @field:SerializedName("id")
    var id: String? = null,
    @field:SerializedName("userName")
    val userName: String? = null,
    @field:SerializedName("password")
    var password: String? = null,
    @field:SerializedName("token")
    var token: String? = null
) {

    init {
        val hashValue = this.password!!.toByteArray()
        val sha256 = MessageDigest.getInstance("SHA-256")
        sha256.update(hashValue)
        val passwordHash = sha256.digest(this.password!!.toByteArray())
        this.password = passwordHash.toString()
    }
}