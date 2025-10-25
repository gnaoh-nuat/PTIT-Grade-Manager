package com.example.ptit_grade_manager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptit_grade_manager.Student.activities.StudentHomepage
import com.example.ptit_grade_manager.Teacher.activities.TeacherMainActivity
import com.example.ptit_grade_manager.model.UserRole
import com.example.ptit_grade_manager.service.AuthService
import com.example.ptit_grade_manager.util.Resource

class SplashActivity : AppCompatActivity() {


    private val authService by lazy { AuthService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        checkUserStatus()
    }

    private fun checkUserStatus() {
        // Kiểm tra xem người dùng đã đăng nhập (Auth) chưa
        val currentUser = authService.getCurrentUser()

        if (currentUser == null) {
            goToLogin()
        } else {
            // Đã đăng nhập (Auth) -> Lấy dữ liệu (Firestore) để biết role
            authService.getUserData(currentUser.uid) { result ->
                when (result) {
                    is Resource.Success -> {
                        // Lấy data thành công
                        val user = result.data
                        when (user.role) {
                            UserRole.STUDENT -> goToHome(UserRole.STUDENT)
                            UserRole.TEACHER -> goToHome(UserRole.TEACHER)
                            else -> {
                                // Lỗi: Không có role
                                Toast.makeText(this, "Không xác định được vai trò", Toast.LENGTH_SHORT).show()
                                goToLogin()
                            }
                        }
                    }
                    is Resource.Error -> {
                        // Lỗi: Có Auth nhưng không có data trên Firestore
                        Toast.makeText(this, "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show()
                        goToLogin()
                    }
                }
            }
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Đóng SplashActivity để người dùng không quay lại được
    }

    private fun goToHome(role: UserRole) {
        val intent = when (role) {
            UserRole.STUDENT -> Intent(this, StudentHomepage::class.java)
            UserRole.TEACHER -> Intent(this, TeacherMainActivity::class.java)
        }
        startActivity(intent)
        finish() // Đóng SplashActivity
    }
}