package com.example.ptit_grade_manager.repository
import com.example.ptit_grade_manager.model.Student
import com.example.ptit_grade_manager.model.Teacher
import com.example.ptit_grade_manager.model.User
import com.example.ptit_grade_manager.model.UserRole
import com.example.ptit_grade_manager.util.Resource // Import này đã đúng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private companion object {
        private const val USERS_COLLECTION = "users"
    }

    // SỬA: Đổi tên tham số từ Result -> Resource
    fun saveStudent(student: Student, password: String, callback: (Resource<Void?>) -> Unit) {
        // Đảm bảo email và role đã được set
        if (student.email == null) {
            // SỬA: Đổi tên lệnh gọi từ Result -> Resource
            callback(Resource.Error(Exception("Email không được để trống")))
            return
        }

        student.role = UserRole.STUDENT

        auth.createUserWithEmailAndPassword(student.email!!, password)
            .addOnSuccessListener { authResult ->
                // Đăng ký Auth thành công, giờ lưu vào Firestore
                val uid = authResult.user?.uid
                if (uid == null) {
                    callback(Resource.Error(Exception("Không thể lấy được UID người dùng.")))
                    return@addOnSuccessListener
                }

                student.id = uid

                // SỬA: Biến `firestore` và `USERS_COLLECTION` giờ đã được định nghĩa
                firestore.collection(USERS_COLLECTION).document(uid)
                    .set(student)
                    .addOnSuccessListener {
                        // Lưu Firestore thành công
                        callback(Resource.Success(null))
                    }
                    .addOnFailureListener { e ->
                        // Lỗi lưu Firestore
                        callback(Resource.Error(e))
                    }
            }
            .addOnFailureListener { e ->
                // Lỗi tạo Auth (ví dụ: email đã tồn tại)
                callback(Resource.Error(e))
            }
    }


    // SỬA: Đổi tên tham số từ Result -> Resource
    fun getUserData(uid: String, callback: (Resource<User>) -> Unit) {
        // SỬA: Biến `firestore` và `USERS_COLLECTION` giờ đã được định nghĩa
        firestore.collection(USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {

                    val roleString = document.getString("role")
                    val role = try {

                        UserRole.valueOf(roleString ?: "")
                    } catch (e: Exception) {
                        callback(Resource.Error(Exception("Vai trò người dùng không hợp lệ.")))
                        return@addOnSuccessListener
                    }

                    // Chuyển đổi (deserialize) document thành object Student hoặc Teacher
                    val user: User? = when (role) {
                        UserRole.STUDENT -> document.toObject(Student::class.java)
                        UserRole.TEACHER -> document.toObject(Teacher::class.java)
                        // Bạn nên xử lý trường hợp role khác nếu có
                    }

                    if (user != null) {
                        callback(Resource.Success(user))
                    } else {
                        callback(Resource.Error(Exception("Không thể phân tích dữ liệu người dùng.")))
                    }
                } else {
                    callback(Resource.Error(Exception("Không tìm thấy người dùng.")))
                }
            }
            .addOnFailureListener { e ->
                callback(Resource.Error(e))
            }
    }
}