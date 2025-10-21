package com.example.ptit_grade_manager.Teacher.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.Teacher.activities.CreateEditClassActivity
import com.example.ptit_grade_manager.Teacher.activities.TeacherClassDetailActivity
// Giả định bạn có Adapter và Model:
// import com.example.ptit_grade_manager.Teacher.adapter.TeacherClassListAdapter
// import com.example.ptit_grade_manager.Teacher.model.ClassInfo
import com.example.ptit_grade_manager.Teacher.model.ClassInfo
import com.example.ptit_grade_manager.Teacher.adapters.TeacherClassListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TeacherClassListFragment : Fragment() {

    private lateinit var classAdapter: TeacherClassListAdapter
    private val classMap = mutableMapOf<String, ClassInfo>()

    // Launcher để nhận kết quả từ CreateEditClassActivity/TeacherClassDetailActivity
    private val createEditClassLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val updatedClass = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("UPDATED_CLASS", ClassInfo::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra<ClassInfo>("UPDATED_CLASS")
            }

            updatedClass?.let {
                classMap[it.id] = it
                updateRecyclerView()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dùng layout Fragment (fragment_class_list.xml)
        return inflater.inflate(R.layout.fragment_class_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvClasses = view.findViewById<RecyclerView>(R.id.rv_classes)
        val fabAddClass = view.findViewById<FloatingActionButton>(R.id.fab_add_class)

        setupRecyclerView(rvClasses)
        loadClasses()

        fabAddClass.setOnClickListener {
            val intent = Intent(requireContext(), CreateEditClassActivity::class.java)
            createEditClassLauncher.launch(intent)
        }
    }

    // Tái tạo lại các hàm logic từ Activity cũ
    private fun setupRecyclerView(rvClasses: RecyclerView) {
        classAdapter = TeacherClassListAdapter(
            onItemClicked = { classInfo ->
                val intent = Intent(requireContext(), TeacherClassDetailActivity::class.java)
                intent.putExtra("CLASS_INFO", classInfo)
                createEditClassLauncher.launch(intent)
            },
            onEditClicked = { classInfo ->
                val intent = Intent(requireContext(), CreateEditClassActivity::class.java)
                intent.putExtra("EXISTING_CLASS", classInfo)
                createEditClassLauncher.launch(intent)
            },
            onDeleteClicked = { classInfo ->
                classMap.remove(classInfo.id)
                updateRecyclerView()
            }
        )
        rvClasses.adapter = classAdapter
        rvClasses.layoutManager = LinearLayoutManager(context)
    }

    private fun loadClasses() {
        // --- Dữ liệu giả lập (Giữ nguyên) ---
        val class1 = ClassInfo(
            id = "cl1",
            code = "INT3306",
            name = "Lập trình Android",
            semester = "2024-2025 | 1",
            credits = 3,
            teacherName = "Nguyễn Văn A"
        )
        val class2 = ClassInfo(
            id = "cl2",
            code = "INT3307",
            name = "An toàn bảo mật thông tin",
            semester = "2024-2025 | 1",
            credits = 3,
            teacherName = "Trần Thị B"
        )

        classMap[class1.id] = class1
        classMap[class2.id] = class2
        // ---
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        classAdapter.submitList(classMap.values.toList())
    }
}