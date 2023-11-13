package com.example.nycschools.models.schools

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class representing school scores information, Parcelable for passing data between components
@Parcelize
data class SchoolScoresItem(
    val dbn: String = "",
    val num_of_sat_test_takers: String = "",
    val sat_critical_reading_avg_score: String = "",
    val sat_math_avg_score: String = "",
    val sat_writing_avg_score: String = "",
    val school_name: String = ""
) : Parcelable
