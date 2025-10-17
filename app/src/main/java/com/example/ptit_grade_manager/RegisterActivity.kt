package com.example.ptit_grade_manager


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etStudentId = findViewById<EditText>(R.id.et_student_id)
        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etDob = findViewById<EditText>(R.id.et_dob) // Date of Birth
        // Thêm RadioGroup cho Giới tính nếu cần
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

            // --- Logic xử lý đăng ký cho sinh viên ---
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
            finish() // Quay lại trang trước (LoginActivity)
        }

        // --- ĐÃ DI CHUYỂN RA ĐÚNG VỊ TRÍ ---
        // Xử lý sự kiện nhấn vào "Quay lại Đăng nhập"
        tvBackToLogin.setOnClickListener {
            finish() // Lệnh này sẽ đóng màn hình hiện tại và quay lại màn hình trước đó
        }
    }
}

