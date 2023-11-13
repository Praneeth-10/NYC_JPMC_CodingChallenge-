package com.example.nycschools.viewmodel

import com.example.nycschools.models.common.onSuccessResponse
import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolListItem
import com.example.nycschools.models.schools.SchoolScores
import com.example.nycschools.models.schools.SchoolScoresItem
import com.example.nycschools.repository.SchoolRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class SchoolsViewModelTest {

    private lateinit var viewModel: SchoolsViewModel
    private val repo = mockk<SchoolRepository>()

    @Before
    fun setup() {
        viewModel = SchoolsViewModel(repo)
    }

    @Test
    fun testGetNYCSchoolsList() = runBlocking {
        val schools = SchoolList()
        schools.add(SchoolListItem("01M292", "Orchard Collegiate Academy (Henry Street School)", "212-406-9411", "mdoyle9@schools.nyc.gov","Manhattan","NY","Some Paragraph","Some NeighborHood","230","www.something.com"))
        every { runBlocking{ repo.getSchools()}} returns flowOf(onSuccessResponse(schools))

        viewModel.getNYCSchoolsList()

        delay(500) // Giving time for the flow collection

        assertEquals(schools, viewModel.schoolList)
        assertFalse(viewModel.loadingIndicatorState.value)
    }

    @Test
    fun testGetSchoolDetails() = runBlocking {
        val dbn = "01M292"
        val scores = SchoolScores()
        scores.add(SchoolScoresItem(dbn, "100", "90", "80", "70", "Orchard Collegiate Academy (Henry Street School)"))
        every { runBlocking{ repo.getSATScores(dbn) } } returns flowOf(onSuccessResponse(scores))

        viewModel.getSchoolDetails(dbn)

        delay(500) // Giving time

        assertEquals(scores[0], viewModel.schoolDetails.value)
        assertFalse(viewModel.loadingIndicatorState.value)
    }
}
