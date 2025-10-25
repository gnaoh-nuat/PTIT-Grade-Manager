package com.example.ptit_grade_manager.Teacher.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.model.ClassInfo
import com.example.ptit_grade_manager.model.StudentInClass

class RankedStudentAdapter(
    private val students: List<StudentInClass>,
    private val classInfo: ClassInfo
) : RecyclerView.Adapter<RankedStudentAdapter.RankedViewHolder>() {

    inner class RankedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_rank_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_rank_name)
        val tvId: TextView = itemView.findViewById(R.id.tv_rank_id)
        val tvScore: TextView = itemView.findViewById(R.id.tv_rank_final_score)
        val tvGrade: TextView = itemView.findViewById(R.id.tv_rank_grade_letter)

        fun bind(student: StudentInClass) {
            val finalScore = student.calculateFinalScore(classInfo.gradeComponents)

            // SỬ DỤNG HỆ ĐIỂM CHỮ CHI TIẾT ĐÃ CẬP NHẬT
            val detailedGrade = student.getDetailedLetterGrade(finalScore)

            // Giả định tải avatar
            ivAvatar.setImageResource(R.drawable.ic_student_avatar)

            tvName.text = student.name
            tvId.text = "Mã SV: ${student.id}"
            tvScore.text = if (finalScore != null) String.format("%.2f", finalScore) else "N/A"
            tvGrade.text = detailedGrade // Hiển thị A+, B+,...
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranked_student_detail, parent, false)
        return RankedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankedViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size
}