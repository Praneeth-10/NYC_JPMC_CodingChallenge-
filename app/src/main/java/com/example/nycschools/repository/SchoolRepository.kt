package com.example.nycschools.repository

import com.example.nycschools.models.apiservice.SchoolService
import com.example.nycschools.models.common.NetworkResponse
import com.example.nycschools.models.common.onFailure
import com.example.nycschools.models.common.onSuccessResponse
import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolScores
import com.example.nycschools.utils.Utils
import kotlinx.coroutines.flow.flow
import okhttp3.internal.Util
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject

class SchoolRepository @Inject constructor(retrofit: Retrofit) {
    // Creating a lazy instance of the SchoolService
    private val schoolsService: SchoolService by lazy {
        retrofit.create(SchoolService::class.java)
    }

    // Function to fetch the list of schools
    suspend fun getSchools() = flow<NetworkResponse> {
        try {
            val response = schoolsService.getSchools()
            if (response.isSuccessful) {
                // Emitting a successful response
                emit(onSuccessResponse(response.body()))
            }
        } catch (e: Exception) {
            // Emitting a failure response with an error message
            emit(onFailure(e.localizedMessage ?: Utils.NetworkUtils.UNKNOWN_EXCEPTION))
        }
    }

    // Function to fetch SAT scores for a school
    suspend fun getSATScores(dbn: String) = flow<NetworkResponse> {
        try {
            val response = schoolsService.getSatScores(dbn)
            if (response.isSuccessful) {
                // Emitting a successful response
                emit(onSuccessResponse(response.body()))
            }
        } catch (e: Exception) {
            // Emitting a failure response with an error message
            emit(onFailure(e.localizedMessage ?: Utils.NetworkUtils.UNKNOWN_EXCEPTION))
        }
    }
}
