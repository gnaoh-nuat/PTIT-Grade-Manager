package com.example.ptit_grade_manager.model

import android.os.Parcelable
import com.example.ptit_grade_manager.model.Student
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Teacher(
    @get:PropertyName("teacher_id")
    @set:PropertyName("teacher_id")
    var teacherId: String? = null,

    @get:PropertyName("faculty")
    @set:PropertyName("faculty")
    var faculty: String? = null,

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String? = null
) : User(), Parcelable  {
    constructor() : this("", "", "")
}
