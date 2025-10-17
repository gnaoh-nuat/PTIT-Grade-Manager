package com.example.ptit_grade_manager


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

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

        // Khai báo các thành phần UI
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


        // Ban đầu chỉ hiển thị bước 1
        showStep(1)

        // Xử lý sự kiện nút "Tiếp tục" (Bước 1)
        btnContinue.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- Logic gửi mã OTP đến email ---
            // Gọi API để yêu cầu gửi mã OTP
            Toast.makeText(this, "Mã OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show()
            tvHeader.text = "Nhập Mã Xác Thực"
            showStep(2)
        }

        // Xử lý sự kiện nút "Xác nhận" (Bước 2)
        btnConfirmOtp.setOnClickListener {
            val otp = etOtp.text.toString().trim()
            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- Logic xác thực mã OTP ---
            // Gọi API để kiểm tra mã OTP có hợp lệ không
            Toast.makeText(this, "Xác thực OTP thành công", Toast.LENGTH_SHORT).show()
            tvHeader.text = "Tạo Mật Khẩu Mới"
            showStep(3)
        }

        // Xử lý sự kiện nút "Hoàn tất" (Bước 3)
        btnFinish.setOnClickListener {
            val newPassword = etNewPassword.text.toString()
            val confirmNewPassword = etConfirmNewPassword.text.toString()

            if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Logic cập nhật mật khẩu mới ---
            // Gọi API để đặt lại mật khẩu cho người dùng
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
            finish() // Kết thúc và quay về trang đăng nhập
        }

        // Xử lý sự kiện nhấn vào "Quay lại Đăng nhập"
        tvBackToLogin.setOnClickListener {
            finish() // Đóng màn hình hiện tại để quay về màn hình đăng nhập
        }
    }

    // Hàm để ẩn/hiện các bước
    private fun showStep(step: Int) {
        step1Layout.visibility = if (step == 1) View.VISIBLE else View.GONE
        step2Layout.visibility = if (step == 2) View.VISIBLE else View.GONE
        step3Layout.visibility = if (step == 3) View.VISIBLE else View.GONE
    }
}

