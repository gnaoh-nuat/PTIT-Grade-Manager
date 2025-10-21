package com.example.ptit_grade_manager.Teacher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Enum để định nghĩa các loại học lực
enum class AcademicRank {
    XUAT_SAC, GIOI, KHA, TRUNG_BINH, YEU, CHUA_CO
}

@Parcelize
data class StudentInClass(
    val id: String,         // Mã sinh viên
    var name: String,
    var gender: String,     // Giới tính
    var avatarUrl: String? = null, // Đường dẫn tới ảnh avatar
    var scores: MutableMap<String, Double?> = mutableMapOf(
        "Chuyên cần" to null,
        "Bài tập lớn" to null,
        "Kiểm tra" to null,
        "Thực hành" to null,
        "Thi cuối kỳ" to null
    )
) : Parcelable {
    // Hàm tính điểm tổng kết (giữ nguyên)
    fun calculateFinalScore(gradeComponents: Map<String, Int>): Double? {
        if (scores.values.any { it == null }) return null
        var totalScore = 0.0
        scores.forEach { (componentName, score) ->
            val weight = gradeComponents[componentName]
            if (weight != null && score != null) {
                totalScore += score * (weight / 100.0)
            }
        }
        return totalScore
    }

    // Hàm chuyển điểm số sang điểm chữ (giữ nguyên)
    fun getFinalGradeLetter(finalScore: Double?): String {
        return when {
            finalScore == null -> "N/A"
            finalScore >= 8.5 -> "A"
            finalScore >= 7.0 -> "B"
            finalScore >= 5.5 -> "C"
            finalScore >= 4.0 -> "D"
            else -> "F"
        }
    }

    // HÀM MỚI: Lấy xếp loại học lực
    fun getAcademicRank(finalScore: Double?): AcademicRank {
        return when {
            finalScore == null -> AcademicRank.CHUA_CO
            finalScore >= 9.0 -> AcademicRank.XUAT_SAC
            finalScore >= 8.0 -> AcademicRank.GIOI
            finalScore >= 6.5 -> AcademicRank.KHA
            finalScore >= 5.0 -> AcademicRank.TRUNG_BINH
            else -> AcademicRank.YEU
        }
    }

    fun getDetailedLetterGrade(finalScore: Double?): String {
        if (finalScore == null) return "N/A"
        return when {
            finalScore >= 9.5 -> "A+"
            finalScore >= 8.5 -> "A"
            finalScore >= 8.0 -> "B+"
            finalScore >= 7.0 -> "B"
            finalScore >= 6.5 -> "C+"
            finalScore >= 5.5 -> "C"
            finalScore >= 5.0 -> "D+"
            finalScore >= 4.0 -> "D"
            else -> "F"
        }
    }
}