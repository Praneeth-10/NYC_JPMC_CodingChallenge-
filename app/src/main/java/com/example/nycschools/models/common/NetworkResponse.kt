package com.example.nycschools.models.common

// Sealed class to represent network responses
sealed class NetworkResponse

// Subclass for successful network response with data
class onSuccessResponse<T>(val data: T) : NetworkResponse()

// Subclass for network response with a failure message
class onFailure(val message: String) : NetworkResponse()

