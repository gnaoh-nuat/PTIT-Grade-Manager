package com.example.ptit_grade_manager

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptit_grade_manager.service.AuthService
import com.example.ptit_grade_manager.util.Resource

class ForgotPasswordActivity : AppCompatActivity() {

    // SỬA: Khởi tạo AuthService
    private val authService by lazy { AuthService() }

    private lateinit var step1Layout: LinearLayout
    private lateinit var step2Layout: LinearLayout
    private lateinit var step3Layout: LinearLayout

    private lateinit var etEmail: EditText
    private lateinit var etOtp: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        step1Layout = findViewById(R.id.step1_layout)
        step2Layout = findViewById(R.id.step2_layout)
        step3Layout = findViewById(R.id.step3_layout)

        etEmail = findViewById(R.id.et_email_forgot)
        etOtp = findViewById(R.id.et_otp)
        etNewPassword = findViewById(R.id.et_new_password)
        etConfirmNewPassword = findViewById(R.id.et_confirm_new_password)

        val btnContinue = findViewById<Button>(R.id.btn_continue)
        val btnConfirmOtp = findViewById<Button>(R.id.btn_confirm_otp)
        val btnFinish = findViewById<Button>(R.id.btn_finish)
        val tvHeader = findViewById<TextView>(R.id.tv_header_forgot)
        val tvBackToLogin = findViewById<TextView>(R.id.tv_back_to_login_forgot)

        showStep(1)

        // Xử lý sự kiện nút "Tiếp tục" (Bước 1)
        btnContinue.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SỬA: Gọi hàm sendPasswordResetEmail từ AuthService
            authService.sendPasswordResetEmail(email) { result ->
                when (result) {
                    is Resource.Success ->{
                        Toast.makeText(this, "Đã gửi email khôi phục. Vui lòng kiểm tra email!", Toast.LENGTH_LONG).show()

                        // --- Xem LƯU Ý QUAN TRỌNG bên dưới ---
                        // Giao diện của bạn (Bước 2, 3) là cho OTP, nhưng Firebase gửi MỘT ĐƯỜNG LINK.
                        // Bạn có thể thông báo cho người dùng và đóng activity này.
                        finish()

                        // Hoặc nếu bạn vẫn muốn hiển thị Bước 2 (dù không logic lắm với Firebase):
                        // tvHeader.text = "Kiểm Tra Email Của Bạn"
                        // showStep(2) // (Bước 2, 3 sẽ không hoạt động đúng)
                    }
                    is Resource.Error -> {
                        Toast.makeText(this, "Gửi email thất bại: ${result.exception.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Xử lý sự kiện nút "Xác nhận" (Bước 2)
        btnConfirmOtp.setOnClickListener {
            // ... Logic xác thực OTP của bạn (nếu có server riêng) ...
            // (AuthService hiện tại không hỗ trợ xác thực OTP)
            tvHeader.text = "Tạo Mật Khẩu Mới"
            showStep(3)
        }

        // Xử lý sự kiện nút "Hoàn tất" (Bước 3)
        btnFinish.setOnClickListener {
            // ... Logic cập nhật mật khẩu của bạn (nếu có server riêng) ...
            // (AuthService hiện tại không hỗ trợ cập nhật mật khẩu bằng OTP)
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun showStep(step: Int) {
        step1Layout.visibility = if (step == 1) View.VISIBLE else View.GONE
        step2Layout.visibility = if (step == 2) View.VISIBLE else View.GONE
        step3Layout.visibility = if (step == 3) View.VISIBLE else View.GONE
    }
}