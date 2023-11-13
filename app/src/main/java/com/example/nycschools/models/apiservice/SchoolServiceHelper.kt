package com.example.nycschools.models.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SchoolServiceHelper {
    //declaring the base URL for accessing the JSON Data
    private const val BASE_URL = "https://data.cityofnewyork.us"

    //building the Retrofit API call for the JSON Data to be fetched
    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}