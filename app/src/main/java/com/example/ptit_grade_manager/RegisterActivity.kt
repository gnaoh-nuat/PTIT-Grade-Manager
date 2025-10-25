package com.example.ptit_grade_manager


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptit_grade_manager.model.Student
import com.example.ptit_grade_manager.model.UserRole
import com.example.ptit_grade_manager.service.AuthService
import com.example.ptit_grade_manager.util.Resource

class RegisterActivity : AppCompatActivity() {

    private val authService by lazy { AuthService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etStudentId = findViewById<EditText>(R.id.et_student_id)
        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etDob = findViewById<EditText>(R.id.et_dob) // Date of Birth
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvBackToLogin = findViewById<TextView>(R.id.tv_back_to_login)

        btnRegister.setOnClickListener {
            val studentId = etStudentId.text.toString().trim()
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val dob = etDob.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (studentId.isEmpty() || name.isEmpty() || email.isEmpty() || dob.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SỬA: Tạo đối tượng Student (vì hàm yêu cầu Student)
            val newStudent = Student(
                id = null, // UID sẽ được gán trong Repository
                studentId = studentId,
                fullName = name,
                email = email,
                dateOfBirth = dob,
                role = UserRole.STUDENT
            )

            // SỬA: Gọi hàm registerStudent từ AuthService
            authService.registerStudent(newStudent, password) { result ->
                when (result) {
                    is Resource.Success<*> -> {
                        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show()
                        finish() // Quay lại trang đăng nhập
                    }
                    is Resource.Error -> {
                        // Hiển thị lỗi cụ thể từ Firebase (ví dụ: email đã tồn tại)
                        Toast.makeText(this, "Đăng ký thất bại: ${result.exception.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}