package com.example.models

import kotlinx.serialization.Serializable

@Serializable
class GenericResponse<out T>(val isSuccess: Boolean, val data: T)