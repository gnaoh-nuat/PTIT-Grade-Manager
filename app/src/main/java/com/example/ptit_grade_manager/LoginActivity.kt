package com.example.ptit_grade_manager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptit_grade_manager.Student.activities.StudentHomepage
import com.example.ptit_grade_manager.Teacher.activities.TeacherMainActivity
import com.example.ptit_grade_manager.model.UserRole
import com.example.ptit_grade_manager.service.AuthService
import com.example.ptit_grade_manager.util.Resource
import com.google.firebase.FirebaseApp

class LoginActivity : AppCompatActivity() {

    // Giữ nguyên, đã đúng
    private val authService by lazy { AuthService() }

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

            // SỬA: Gọi hàm login từ AuthService và xử lý callback Result<User>
            authService.login(email, password) { result ->
                when (result) {
                    is Resource.Success -> {
                        val user = result.data // 'user' bây giờ sẽ được hiểu là kiểu 'User'
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()

                        // Dòng này sẽ hết lỗi
                        when (user.role) {
                            UserRole.STUDENT -> {
                                val intent = Intent(this, StudentHomepage::class.java)
                                startActivity(intent)
                                finish()
                            }

                            UserRole.TEACHER -> {
                                val intent = Intent(this, TeacherMainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            else -> {
                                Toast.makeText(
                                    this,
                                    "Không xác định được vai trò người dùng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    // Đăng nhập hoặc lấy dữ liệu thất bại
                    is Resource.Error -> {
                        Toast.makeText(this, "Email hoặc mật khẩu không chính xác: ${result.exception.message}", Toast.LENGTH_LONG).show()
                    }
                }
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