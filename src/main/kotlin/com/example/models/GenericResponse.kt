package com.example.models

class GenericResponse<out T>(val isSuccess: Boolean, data: T)