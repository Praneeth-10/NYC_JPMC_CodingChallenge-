package com.example.nycschools.models.apiservice

import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolScores
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Interface for consuming APIs related to school data
interface SchoolService {
    // Function to fetch a list of schools from the specified API endpoint
    @GET("/resource/s3k6-pzi2.json")
    suspend fun getSchools(): Response<SchoolList>

    // Function to fetch SAT scores for a specific school based on its "dbn" parameter
    @GET("/resource/f9bf-2cp4.json")
    suspend fun getSatScores(@Query("dbn") dbn: String): Response<SchoolScores>
}
