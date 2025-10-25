package com.example.ptit_grade_manager.Teacher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.model.StudentInClass

class TeacherStudentListAdapter(
    private var studentList: MutableList<StudentInClass>, // THAY ĐỔI: Dùng MutableList
    private val onItemClicked: (StudentInClass) -> Unit,
    private val onDeleteClicked: (StudentInClass) -> Unit
) : RecyclerView.Adapter<TeacherStudentListAdapter.StudentViewHolder>() {

    // Lớp ViewHolder để giữ các tham chiếu đến view của một item
    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        private val tvStudentId: TextView = itemView.findViewById(R.id.tv_student_id)
        private val btnDetails: ImageButton = itemView.findViewById(R.id.btn_view_details)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_student)

        // Hàm bind để gán dữ liệu và sự kiện cho view
        fun bind(student: StudentInClass) {
            tvStudentName.text = student.name
            tvStudentId.text = student.id

            // Gán sự kiện click cho toàn bộ item
            itemView.setOnClickListener { onItemClicked(student) }

            // Nút "chi tiết" (để sửa điểm) cũng gọi cùng một sự kiện
            btnDetails.setOnClickListener { onItemClicked(student) }

            // Gán sự kiện click cho nút xóa
            btnDelete.setOnClickListener { onDeleteClicked(student) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_in_class_simplified, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position])
    }

    override fun getItemCount() = studentList.size

    // HÀM MỚI: Dùng để cập nhật danh sách từ bên ngoài
    fun updateStudents(newStudents: List<StudentInClass>) {
        studentList.clear()
        studentList.addAll(newStudents)
        notifyDataSetChanged()
    }
}