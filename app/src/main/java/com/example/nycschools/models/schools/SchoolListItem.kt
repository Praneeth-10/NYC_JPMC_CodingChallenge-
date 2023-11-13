package com.example.nycschools.models.schools

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class representing a school item, Parcelable for passing data between components
@Parcelize
data class SchoolListItem(
    val dbn: String,
    val school_name: String,
    val phone_number: String,
    val school_email: String,
    val city: String,
    val state_code: String,
    val overview_paragraph: String,
    val neighborhood: String,
    val total_students: String,
    val website : String
) : Parcelable
