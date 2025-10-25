package com.example.ptit_grade_manager.Teacher.activities

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.Teacher.adapters.RankedStudentsDialog
import com.example.ptit_grade_manager.Teacher.adapters.TeacherStudentListAdapter
import com.example.ptit_grade_manager.model.AcademicRank
import com.example.ptit_grade_manager.model.ClassInfo
import com.example.ptit_grade_manager.model.StudentInClass
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TeacherClassDetailActivity : AppCompatActivity(), EditStudentScoreSheet.ScoreUpdateListener {

    private var classInfo: ClassInfo? = null
    private lateinit var studentAdapter: TeacherStudentListAdapter
    private lateinit var tvTeacherName: TextView
    private lateinit var tvStudentCount: TextView
    private lateinit var tvClassCode: TextView // KHAI BÁO MÃ HP
    private lateinit var tvCredits: TextView   // KHAI BÁO SỐ TÍN CHỈ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Khởi tạo các TextView cần dùng
        tvTeacherName = findViewById(R.id.tv_teacher_name)
        tvStudentCount = findViewById(R.id.tv_student_count)

        // GÁN GIÁ TRỊ VÀ KHỞI TẠO CÁC VIEW MỚI
        tvClassCode = findViewById(R.id.tv_class_code)
        tvCredits = findViewById(R.id.tv_credits)

        classInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("CLASS_INFO", ClassInfo::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("CLASS_INFO")
        }


        if (classInfo != null) {
            // HIỂN THỊ THÔNG TIN CHUNG
            findViewById<TextView>(R.id.tv_detail_class_name).text = classInfo!!.name

            // DÙNG CÁC BIẾN MỚI ĐỂ HIỂN THỊ DỮ LIỆU
            tvClassCode.text = "Mã HP: ${classInfo!!.code}"
            tvCredits.text = "Số tín chỉ: ${classInfo!!.credits}"
            tvTeacherName.text = "Giảng viên: ${classInfo!!.teacherName}"

            // ---- DỮ LIỆU GIẢ LẬP CHO SINH VIÊN (NẾU CẦN) ----
            if (classInfo!!.students.isEmpty()) {
                // Giả định ClassInfo đã có teacherName="Giảng viên A"
                classInfo!!.students.addAll(listOf(
                    StudentInClass("B20DCCN001", "Nguyễn Văn An", "Nam"),
                    StudentInClass("B20DCCN002", "Trần Thị Bình", "Nữ")
                ))
            }

            setupRecyclerView()
            updateStatistics() // Hàm này sẽ gọi cập nhật số lượng sinh viên

            // THÊM LOGIC CHO NÚT ADD
            val fabAddStudent = findViewById<FloatingActionButton>(R.id.fab_add_student)
            fabAddStudent.setOnClickListener {
                showAddStudentDialog()
            }
        }
    }


    private fun setupRecyclerView() {
        val rvStudents = findViewById<RecyclerView>(R.id.rv_students_in_class)
        studentAdapter = TeacherStudentListAdapter(
            studentList = classInfo!!.students,

            onItemClicked = { student ->
                val bottomSheet = EditStudentScoreSheet.newInstance(student, classInfo!!.gradeComponents)
                bottomSheet.setScoreUpdateListener(this)
                bottomSheet.show(supportFragmentManager, "EditScoreSheet")
            },

            onDeleteClicked = { student ->
                showDeleteConfirmationDialog(student)
            }
        )
        rvStudents.adapter = studentAdapter
        rvStudents.layoutManager = LinearLayoutManager(this)
    }

    // HÀM MỚI: Hiển thị dialog xác nhận xóa
    private fun showDeleteConfirmationDialog(student: StudentInClass) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.name} (${student.id}) khỏi lớp học?")
            .setPositiveButton("Xóa") { _, _ ->
                val position = classInfo!!.students.indexOf(student)
                if (position != -1) {
                    classInfo!!.students.removeAt(position)
                    studentAdapter.notifyItemRemoved(position)
                    updateStatistics() // Cập nhật lại thống kê VÀ SỐ LƯỢNG SINH VIÊN
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    private fun showAddStudentDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_student, null)
        val etStudentId = dialogView.findViewById<EditText>(R.id.et_student_id)
        val etStudentName = dialogView.findViewById<EditText>(R.id.et_student_name)

        // --- PHẦN CẢI THIỆN ---
        // 1. Lấy đúng AutoCompleteTextView
        val actvStudentGender = dialogView.findViewById<AutoCompleteTextView>(R.id.actv_student_gender)

        // 2. Tạo danh sách các lựa chọn giới tính
        val genders = arrayOf("Nam", "Nữ", "Khác")

        // 3. Tạo ArrayAdapter và gán cho AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        actvStudentGender.setAdapter(adapter)
        // --- KẾT THÚC PHẦN CẢI THIỆN ---

        builder.setView(dialogView)
            .setTitle("Thêm sinh viên mới")
            .setPositiveButton("Thêm") { dialog, _ ->
                val id = etStudentId.text.toString().trim()
                val name = etStudentName.text.toString().trim()
                val gender = actvStudentGender.text.toString().trim() // Lấy text từ AutoCompleteTextView

                if (id.isNotEmpty() && name.isNotEmpty() && gender.isNotEmpty()) {
                    val newStudent = StudentInClass(id, name, gender)
                    classInfo!!.students.add(newStudent)
                    studentAdapter.notifyItemInserted(classInfo!!.students.size - 1)
                    updateStatistics()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun onScoreUpdated() {
        // Cập nhật từng item thay vì cả list để có hiệu ứng đẹp hơn
        studentAdapter.notifyDataSetChanged()
        updateStatistics() // Cập nhật lại thống kê VÀ SỐ LƯỢNG SINH VIÊN
    }

    private fun updateStatistics() {
        // CẬP NHẬT TỔNG SỐ SINH VIÊN Ở ĐÂY
        tvStudentCount.text = "Tổng số sinh viên: ${classInfo!!.students.size}"

        val studentRanks = classInfo!!.students.map {
            it.getAcademicRank(it.calculateFinalScore(classInfo!!.gradeComponents))
        }

        updateStatBox(findViewById(R.id.stat_excellent), "Xuất sắc", studentRanks.count { it == AcademicRank.XUAT_SAC }, R.color.excellent_color)
        updateStatBox(findViewById(R.id.stat_good), "Giỏi", studentRanks.count { it == AcademicRank.GIOI }, R.color.good_color)
        updateStatBox(findViewById(R.id.stat_fair), "Khá", studentRanks.count { it == AcademicRank.KHA }, R.color.fair_color)
        updateStatBox(findViewById(R.id.stat_average), "TB", studentRanks.count { it == AcademicRank.TRUNG_BINH }, R.color.average_color)
        updateStatBox(findViewById(R.id.stat_poor), "Yếu", studentRanks.count { it == AcademicRank.YEU }, R.color.poor_color)
    }


    private fun updateStatBox(view: View, label: String, count: Int, colorRes: Int) {
        view.findViewById<TextView>(R.id.tv_stat_label).text = label
        view.findViewById<TextView>(R.id.tv_stat_count).text = count.toString()
        view.setBackgroundResource(colorRes)

        val academicRank = when (label) {
            "Xuất sắc" -> AcademicRank.XUAT_SAC
            "Giỏi" -> AcademicRank.GIOI
            "Khá" -> AcademicRank.KHA
            "TB" -> AcademicRank.TRUNG_BINH
            "Yếu" -> AcademicRank.YEU
            else -> AcademicRank.CHUA_CO
        }

        view.setOnClickListener {
            if (count > 0) {
                // GỌI HÀM MỚI Ở ĐÂY
                showRankedStudentsList(academicRank, label)
            } else {
                Toast.makeText(this, "Không có sinh viên nào đạt xếp loại $label", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // HÀM MỚI: Hiển thị danh sách sinh viên đã lọc bằng DialogFragment
    private fun showRankedStudentsList(rank: AcademicRank, rankName: String) {
        val filteredStudents = classInfo!!.students.filter {
            it.getAcademicRank(it.calculateFinalScore(classInfo!!.gradeComponents)) == rank
        }

        // Kiểm tra xem Activity có còn tồn tại không
        if (isFinishing || classInfo == null) return

        val dialog = RankedStudentsDialog.newInstance(
            students = filteredStudents,
            rankName = rankName,
            classInfo = classInfo!!
        )
        dialog.show(supportFragmentManager, "RankedListDialog")
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}