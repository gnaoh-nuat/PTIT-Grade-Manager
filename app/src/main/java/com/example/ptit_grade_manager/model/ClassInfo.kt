package com.example.ptit_grade_manager.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ClassInfo(

    val id: String = UUID.randomUUID().toString(),

    var code: String,

    var name: String,

    var semester: String,

    var credits: Int,

    @get:PropertyName("teacher_name")
    @set:PropertyName("teacher_name")
    var teacherName: String,

    // Quản lý các đầu điểm và phần trăm tương ứng, có thể chỉnh sửa
    var gradeComponents: MutableMap<String, Int> = mutableMapOf(
        "Chuyên cần" to 10,
        "Bài tập lớn" to 10,
        "Kiểm tra" to 10,
        "Thực hành" to 10,
        "Thi cuối kỳ" to 60
    ),
    // Danh sách sinh viên trong lớp học này
    var students: MutableList<StudentInClass> = mutableListOf()
) : Parcelable