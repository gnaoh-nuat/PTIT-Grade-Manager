package com.example.ptit_grade_manager.Teacher.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.model.ClassInfo
import com.example.ptit_grade_manager.model.StudentInClass
import androidx.recyclerview.widget.DividerItemDecoration

class RankedStudentsDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Yêu cầu cửa sổ không có tiêu đề mặc định
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_ranked_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val students = arguments?.getParcelableArrayList<StudentInClass>("students_list") ?: emptyList()
        val rankName = arguments?.getString("rank_name") ?: "Danh sách"
        val classInfo = arguments?.getParcelable<ClassInfo>("class_info")

        val rv = view.findViewById<RecyclerView>(R.id.rv_ranked_students)
        val tvTitle = view.findViewById<TextView>(R.id.tv_dialog_title)
        val btnClose = view.findViewById<Button>(R.id.btn_dialog_close)

        tvTitle.text = "Danh sách: $rankName (${students.size})"
        btnClose.setOnClickListener { dismiss() }


        if (classInfo != null) {
            val adapter = RankedStudentAdapter(students, classInfo)
            rv.adapter = adapter
            val layoutManager = LinearLayoutManager(context)
            rv.layoutManager = layoutManager

            // PHẦN BỔ SUNG ĐÃ ĐÚNG: THÊM VẠCH CHIA
            val dividerItemDecoration = DividerItemDecoration(
                rv.context,
                layoutManager.orientation
            )
            rv.addItemDecoration(dividerItemDecoration) // Đã thêm
        }
    }

    // PHƯƠNG THỨC MỚI: ĐẶT KÍCH THƯỚC TOÀN MÀN HÌNH
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        fun newInstance(
            students: List<StudentInClass>,
            rankName: String,
            classInfo: ClassInfo
        ): RankedStudentsDialog {
            val args = Bundle().apply {
                putParcelableArrayList("students_list", ArrayList(students))
                putString("rank_name", rankName)
                putParcelable("class_info", classInfo)
            }
            return RankedStudentsDialog().apply {
                arguments = args
            }
        }
    }
}