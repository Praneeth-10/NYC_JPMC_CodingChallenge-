package com.example.nycschools.models.schools

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SchoolList : ArrayList<SchoolListItem>(),Parcelable