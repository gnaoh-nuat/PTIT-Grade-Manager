package com.example.ptit_grade_manager.Teacher.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.Teacher.model.ClassInfo
import java.util.UUID

class CreateEditClassActivity : AppCompatActivity() {

    private var existingClass: ClassInfo? = null
    // Map để lưu trữ tham chiếu đến các EditText nhập % điểm
    private val componentEditTexts = mutableMapOf<String, EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dùng tên layout đã thống nhất: activity_class_editor
        // *Đã sửa lỗi này trong phiên bản cuối cùng*
        setContentView(R.layout.activity_create_edit_class)

        // BƯỚC 1: SETUP TOOLBAR
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Nhận dữ liệu lớp học nếu là chế độ sửa
        existingClass = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXISTING_CLASS", ClassInfo::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXISTING_CLASS")
        }

        // Đặt tiêu đề cho Toolbar dựa vào chế độ (Tạo mới hay Chỉnh sửa)
        supportActionBar?.title = if (existingClass == null) "Tạo lớp học mới" else "Chỉnh sửa thông tin"

        // Khởi tạo Views
        val etClassName = findViewById<EditText>(R.id.et_class_name)
        val etClassCode = findViewById<EditText>(R.id.et_class_code)
        val etSemester = findViewById<EditText>(R.id.et_semester)
        val etCredits = findViewById<EditText>(R.id.et_credits)
        val btnSave = findViewById<Button>(R.id.btn_save_class)
        val gradeComponentsLayout = findViewById<LinearLayout>(R.id.ll_grade_components)

        // Lấy cấu trúc điểm để hiển thị
        val componentsToShow = existingClass?.gradeComponents ?: ClassInfo(
            code = "", name = "", semester = "", credits = 0, teacherName = "Giảng viên A"
        ).gradeComponents

        // TẠO ĐỘNG CÁC Ô NHẬP % ĐIỂM
        componentsToShow.forEach { (name, weight) ->
            // SỬ DỤNG ID layout CON CHUẨN
            val componentView = layoutInflater.inflate(R.layout.item_grade_component_edit, gradeComponentsLayout, false)

            // LẤY THAM CHIẾU CÁC VIEW CON BẰNG ID CHUẨN
            val tvComponentName = componentView.findViewById<TextView>(R.id.tv_component_name)
            val etComponentWeight = componentView.findViewById<EditText>(R.id.et_component_weight)

            tvComponentName.text = name

            // LOGIC QUAN TRỌNG: Chỉ điền giá trị cũ nếu là chế độ SỬA
            if (existingClass != null) {
                etComponentWeight.setText(weight.toString())
            }

            componentEditTexts[name] = etComponentWeight
            gradeComponentsLayout.addView(componentView)
        }


        // ĐIỀN THÔNG TIN CŨ VÀO TRƯỜNG NHẬP (nếu là chế độ sửa)
        existingClass?.let {
            etClassName.setText(it.name)
            etClassCode.setText(it.code)
            etSemester.setText(it.semester)
            etCredits.setText(it.credits.toString())
        }

        // XỬ LÝ SỰ KIỆN LƯU LẠI
        btnSave.setOnClickListener {
            // Lấy thông tin cơ bản
            val name = etClassName.text.toString().trim()
            val code = etClassCode.text.toString().trim()
            val semester = etSemester.text.toString().trim()
            val creditsStr = etCredits.text.toString().trim()
            val credits = creditsStr.toIntOrNull()

            if (name.isEmpty() || code.isEmpty() || semester.isEmpty() || credits == null) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin cơ bản và Số tín chỉ hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lấy và kiểm tra thông tin điểm thành phần
            val updatedComponents = mutableMapOf<String, Int>()
            var totalWeight = 0
            var allComponentsValid = true

            componentEditTexts.forEach { (componentName, editText) ->
                val weightStr = editText.text.toString().trim()
                val weight = weightStr.toIntOrNull()

                if (weight == null || weight < 0 || weight > 100) {
                    editText.error = "Tỷ lệ không hợp lệ (0-100) hoặc bị bỏ trống"
                    allComponentsValid = false
                    return@forEach
                }
                updatedComponents[componentName] = weight
                totalWeight += weight
            }

            if (!allComponentsValid) {
                Toast.makeText(this, "Vui lòng kiểm tra lại tỷ lệ % đầu điểm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (totalWeight != 100) {
                Toast.makeText(this, "Tổng tỷ lệ % các đầu điểm phải bằng 100 (Hiện tại: $totalWeight)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Tạo hoặc cập nhật đối tượng ClassInfo
            val resultClass = existingClass?.apply {
                this.name = name
                this.code = code
                this.semester = semester
                this.credits = credits
                this.gradeComponents = updatedComponents
            } ?: ClassInfo(
                id = UUID.randomUUID().toString(),
                name = name,
                code = code,
                semester = semester,
                credits = credits,
                teacherName = "Giảng viên A",
                gradeComponents = updatedComponents
            )

            // Trả kết quả về cho TeacherClassListActivity
            val resultIntent = Intent().apply {
                putExtra("UPDATED_CLASS", resultClass)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // XỬ LÝ SỰ KIỆN NHẤN NÚT BACK TRÊN TOOLBAR
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
