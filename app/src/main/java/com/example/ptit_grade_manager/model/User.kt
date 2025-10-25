package com.example.ptit_grade_manager.model


import android.os.Parcelable
import com.example.ptit_grade_manager.model.Student
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize



@Parcelize
open class User(

     var id: String? = null,

    @get:PropertyName("full_name")
    @set:PropertyName("full_name")
     var fullName: String? = null,

     var email: String? = null,

     var password: String? = null,

    @get:PropertyName("phone_number")
    @set:PropertyName("phone_number")
     var phoneNumber: String? = null,

    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
     var avatarUrl: String? = null,

     var role: UserRole? = null

) : Parcelable  {
    constructor() : this("", "", "", "", "", "",UserRole.STUDENT)
}

enum class UserRole {
    STUDENT,
    TEACHER
}

