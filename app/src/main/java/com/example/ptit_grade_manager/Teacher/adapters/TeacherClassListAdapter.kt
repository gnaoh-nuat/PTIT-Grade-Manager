package com.example.ptit_grade_manager.Teacher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.Teacher.model.ClassInfo


class TeacherClassListAdapter(
    private val onItemClicked: (ClassInfo) -> Unit,
    private val onEditClicked: (ClassInfo) -> Unit,
    private val onDeleteClicked: (ClassInfo) -> Unit
) : ListAdapter<ClassInfo, TeacherClassListAdapter.ClassViewHolder>(ClassDiffCallback()) {

    // Lớp ViewHolder để giữ các tham chiếu đến view của một item
    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvClassName: TextView = itemView.findViewById(R.id.tv_class_name)
        private val tvClassCode: TextView = itemView.findViewById(R.id.tv_class_code)
        private val tvClassSemester: TextView = itemView.findViewById(R.id.tv_class_semester)
        private val btnEdit: ImageView = itemView.findViewById(R.id.btn_edit_class)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete_class)

        // Hàm bind để gán dữ liệu từ ClassInfo lên các view
        fun bind(classInfo: ClassInfo) {
            tvClassName.text = classInfo.name
            tvClassCode.text = "Mã HP: ${classInfo.code}"
            tvClassSemester.text = "Kỳ: ${classInfo.semester}"

            // Gán sự kiện click cho toàn bộ item và các nút
            itemView.setOnClickListener { onItemClicked(classInfo) }
            btnEdit.setOnClickListener { onEditClicked(classInfo) }
            btnDelete.setOnClickListener { onDeleteClicked(classInfo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// Lớp DiffUtil giúp RecyclerView cập nhật danh sách một cách thông minh
class ClassDiffCallback : DiffUtil.ItemCallback<ClassInfo>() {
    override fun areItemsTheSame(oldItem: ClassInfo, newItem: ClassInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClassInfo, newItem: ClassInfo): Boolean {
        return oldItem == newItem
    }
}