package com.example.ptit_grade_manager


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptit_grade_manager.Student.activities.StudentHomepage
import com.example.ptit_grade_manager.Teacher.activities.TeacherHomepage

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Logic xử lý đăng nhập ---
            // Ở đây, bạn sẽ gọi API hoặc kiểm tra với Firebase Authentication
            // Dựa vào email, xác định đây là tài khoản sinh viên hay giáo viên

            // Giả lập logic: nếu email chứa "student", chuyển đến trang sinh viên
            // Ngược lại, chuyển đến trang giáo viên
            if (email.contains("student")) {
                Toast.makeText(this, "Đăng nhập thành công với tư cách Sinh viên", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, StudentHomepage::class.java)
                startActivity(intent)
                finish()
            } else if(email.contains("teacher")) {
                Toast.makeText(this, "Đăng nhập thành công với tư cách Giáo viên", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TeacherHomepage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}


