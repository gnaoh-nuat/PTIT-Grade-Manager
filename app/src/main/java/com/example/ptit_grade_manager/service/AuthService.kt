package com.example.ptit_grade_manager.service

import com.example.ptit_grade_manager.util.Resource
import com.example.ptit_grade_manager.model.Student
import com.example.ptit_grade_manager.model.User
import com.example.ptit_grade_manager.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthService(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {
    fun login(email: String, password: String, callback: (Resource<User>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    // Đăng nhập Auth thành công, lấy dữ liệu Firestore
                    userRepository.getUserData(uid, callback)
                } else {
                    // SỬA: Đổi tên lệnh gọi từ Result -> Resource
                    callback(Resource.Error(Exception("Không thể lấy UID sau khi đăng nhập.")))
                }
            }
            .addOnFailureListener { e ->
                // Lỗi đăng nhập Auth (sai pass, user không tồn tại)
                // SỬA: Đổi tên lệnh gọi từ Result -> Resource
                callback(Resource.Error(e))
            }
    }

    // SỬA: Đổi tên tham số từ Result -> Resource
    fun registerStudent(student: Student, password: String, callback: (Resource<Void?>) -> Unit) {
        userRepository.saveStudent(student, password, callback)
    }

    // SỬA: Đổi tên tham số từ Result -> Resource
    fun sendPasswordResetEmail(email: String, callback: (Resource<Void?>) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                // SỬA: Đổi tên lệnh gọi từ Result -> Resource
                callback(Resource.Success(null))
            }
            .addOnFailureListener { e ->
                // SỬA: Đổi tên lệnh gọi từ Result -> Resource
                callback(Resource.Error(e))
            }
    }

    /**
     * Kiểm tra người dùng hiện tại đã đăng nhập chưa.
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun getUserData(uid: String, callback: (Resource<User>) -> Unit) {
        userRepository.getUserData(uid, callback)
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}