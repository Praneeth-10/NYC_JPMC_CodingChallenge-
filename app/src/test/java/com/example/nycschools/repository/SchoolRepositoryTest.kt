package com.example.nycschools.repository

import com.example.nycschools.models.common.onSuccessResponse
import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolScores
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SchoolRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var schoolRepository: SchoolRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        schoolRepository = SchoolRepository(retrofit)
    }

    @Test
    fun `getSchools returns expected data`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[{\"dbn\":\"01M292\",\"school_name\":\"Henry Street School for International Studies\",\"phone_number\":\"212-406-9411\",\"school_email\":\"01M292@schools.nyc.gov\"}]")
        mockWebServer.enqueue(mockResponse)

        // Act
        val result = schoolRepository.getSchools()

        // Assert
        result.collect { response ->
            when (response) {
                is onSuccessResponse<*> -> {
                    val data = response.data as SchoolList
                    assert(data.size == 1)
                    assert(data[0].dbn == "01M292")
                    assert(data[0].school_name == "Henry Street School for International Studies")
                    assert(data[0].phone_number == "212-406-9411")
                    assert(data[0].school_email == "01M292@schools.nyc.gov")
                }
                else -> throw Exception("Test failed")
            }
        }
    }

    @Test
    fun `getSATScores returns expected data`() = runBlocking {
        // Arrange
        val dbn = "01M292"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[{\"dbn\":\"01M292\",\"num_of_sat_test_takers\":\"29\",\"sat_critical_reading_avg_score\":\"355\",\"sat_math_avg_score\":\"404\",\"sat_writing_avg_score\":\"363\",\"school_name\":\"Henry Street School for International Studies\"}]")
        mockWebServer.enqueue(mockResponse)

        // Act
        val result = schoolRepository.getSATScores(dbn)

        // Assert
        result.collect { response ->
            when (response) {
                is onSuccessResponse<*> -> {
                    val data = response.data as SchoolScores
                    assert(data.size == 1)
                    assert(data[0].dbn == "01M292")
                    assert(data[0].num_of_sat_test_takers == "29")
                    assert(data[0].sat_critical_reading_avg_score == "355")
                    assert(data[0].sat_math_avg_score == "404")
                    assert(data[0].sat_writing_avg_score == "363")
                    assert(data[0].school_name == "Henry Street School for International Studies")
                }
                else -> throw Exception("Test failed")
            }
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}
