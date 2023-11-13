package com.example.nycschools.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nycschools.models.common.onSuccessResponse
import com.example.nycschools.models.schools.SchoolList
import com.example.nycschools.models.schools.SchoolListItem
import com.example.nycschools.models.schools.SchoolScores
import com.example.nycschools.models.schools.SchoolScoresItem
import com.example.nycschools.repository.SchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolsViewModel @Inject constructor(private val repo: SchoolRepository) : ViewModel() {
    // Mutable state for storing the list of schools
    val schoolList = mutableStateListOf<SchoolListItem>()
    // Mutable state for storing school details
    val schoolDetails = mutableStateOf(SchoolScoresItem())
    // Mutable state for managing loading indicator state
    val loadingIndicatorState = mutableStateOf(true)
    // Mutable state for tracking and storing error information
    val errorState = mutableStateOf<Exception?>(null)

    val searchQuery = mutableStateOf("")

    // Make the filtered list a derived state of schoolList and searchQuery
    val filteredSchoolList = derivedStateOf {
        schoolList.filter { it.city.contains(searchQuery.value, ignoreCase = true) }
    }

    // Function to fetch the list of NYC schools
    fun getNYCSchoolsList() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSchools().collect { response ->
                when (response) {
                    is onSuccessResponse<*> -> {
                        setLoading(false)
                        schoolList.addAll(
                            ((response as onSuccessResponse<*>).data) as SchoolList
                        )
                    }
                    else -> {
                        // Showing a failure message to the user and throwing an exception
                        throw Exception("No data found")
                    }
                }
            }
        }
    }

    fun setLoading(check:Boolean){
        loadingIndicatorState.value = check
    }

    // Function to fetch school details based on DBN
    fun getSchoolDetails(dbn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getSATScores(dbn).collect { response ->
                    when (response) {
                        is onSuccessResponse<*> -> {
                            val scoresList = (((response as onSuccessResponse<*>).data) as SchoolScores)
                            setLoading(false)
                            // Checking for the size of the scores list
                            if (scoresList.size >= 1) {
                                schoolDetails.value = scoresList[0]
                            } else {
                                // Setting schoolDetails with "No Data Found" if no data is available
                                schoolDetails.value = SchoolScoresItem(
                                    dbn,
                                    "No Data Found",
                                    "No Data Found",
                                    "No Data Found",
                                    "No Data Found",
                                    "No Data Found"
                                )
                            }
                        }
                        else -> {
                            // Throwing an exception in case of an error
                            throw Exception("An error occurred while fetching school details")
                        }
                    }
                }
            } catch (e: Exception) {
                // Capturing and store exceptions in the errorState
                errorState.value = e
            }
        }
    }
}