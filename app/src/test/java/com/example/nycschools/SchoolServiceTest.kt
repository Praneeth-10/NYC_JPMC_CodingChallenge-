package com.example.nycschools

import com.example.nycschools.models.apiservice.SchoolService
import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolListItem
import com.example.nycschools.models.schools.SchoolScores
import com.example.nycschools.models.schools.SchoolScoresItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(Parameterized::class)
class SchoolServiceTest(private val dbn: String, private val school_name: String) {

    @Mock
    private lateinit var mockSchoolService: SchoolService

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("dbn1", "school_name1"),
                arrayOf("dbn2", "school_name2"),
            )
        }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetSchools() = runBlocking {
        val mockSchoolListItem = SchoolListItem(
            dbn = dbn,
            school_name = "school_Name",
            phone_number = "phone_Number",
            school_email = "school_Email",
            "Manhattan","NY","Some Paragraph","Some NeighborHood","230","www.something.com"
        )
        val mockSchoolList = SchoolList()
        mockSchoolList.add(mockSchoolListItem)
        val response = Response.success(mockSchoolList)

        `when`(mockSchoolService.getSchools()).thenReturn(response)

        val result = mockSchoolService.getSchools()
        assertEquals(response, result)
    }

    @Test
    fun testGetSatScores() = runBlocking {
        val mockSchoolScoresItem = SchoolScoresItem(
            dbn = dbn,
            num_of_sat_test_takers = "num_of_sat_test_takers",
            sat_critical_reading_avg_score = "sat_critical_reading_avg_score",
            sat_math_avg_score = "sat_math_avg_score",
            sat_writing_avg_score = "sat_writing_avg_score",
            school_name = school_name
        )
        val mockSchoolScores = SchoolScores()
        mockSchoolScores.add(mockSchoolScoresItem)
        val response = Response.success(mockSchoolScores)

        `when`(mockSchoolService.getSatScores(dbn)).thenReturn(response)

        val result = mockSchoolService.getSatScores(dbn)
        assertEquals(response, result)
    }
}
