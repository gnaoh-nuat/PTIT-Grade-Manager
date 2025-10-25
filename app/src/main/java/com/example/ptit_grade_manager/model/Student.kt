package com.example.ptit_grade_manager.model


import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Student(
    @get:PropertyName("student_id")
    @set:PropertyName("student_id")
    var studentId: String? = null,

    @get:PropertyName("gender")
    @set:PropertyName("gender")
    var gender: String? = null,

    @get:PropertyName("date_of_birth")
    @set:PropertyName("date_of_birth")
    var dateOfBirth: String? = null
) : User(), Parcelable {

    constructor() : this("", "", "")

    constructor(
        id: String?,
        studentId: String?,
        fullName: String?,
        email: String?,
        dateOfBirth: String?,
        role: UserRole?
    ) : this( // 1. Gọi constructor chính của data class
        studentId = studentId,
        gender = null, // Form đăng ký không có gender, gán tạm là null
        dateOfBirth = dateOfBirth
    ) {
        // 2. Gán các thuộc tính kế thừa từ lớp cha 'User'
        this.id = id
        this.fullName = fullName
        this.email = email
        this.role = role
        // (Không cần gán password, phoneNumber, avatarUrl vì chúng là null)
    }
}
