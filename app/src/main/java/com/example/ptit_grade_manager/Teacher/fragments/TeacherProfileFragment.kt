package com.example.ptit_grade_manager.Teacher.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ptit_grade_manager.R
// import com.bumptech.glide.Glide

class TeacherProfileFragment : Fragment() {

    private lateinit var ivProfileAvatar: ImageView
    private lateinit var etTeacherName: EditText
    private lateinit var etTeacherEmail: EditText

    // KHAI BÁO CÁC BIẾN MỚI
    private lateinit var etTeacherFaculty: EditText
    private lateinit var etTeacherTitle: EditText
    private lateinit var etTeacherPhone: EditText
    // KẾT THÚC KHAI BÁO MỚI

    // Launcher để chọn ảnh
    private val imageChooserLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let {
                // Tải ảnh vào ImageView
                ivProfileAvatar.setImageURI(it)
                Toast.makeText(context, "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dùng layout Fragment mới (fragment_teacher_profile.xml)
        return inflater.inflate(R.layout.fragment_teacher_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfileAvatar = view.findViewById(R.id.iv_profile_avatar)
        etTeacherName = view.findViewById(R.id.et_teacher_name)
        val etTeacherId = view.findViewById<EditText>(R.id.et_teacher_id)
        etTeacherEmail = view.findViewById(R.id.et_teacher_email)

        // GÁN VIEW CHO CÁC BIẾN MỚI
        etTeacherFaculty = view.findViewById(R.id.et_teacher_faculty)
        etTeacherTitle = view.findViewById(R.id.et_teacher_title)
        etTeacherPhone = view.findViewById(R.id.et_teacher_phone)
        // KẾT THÚC GÁN VIEW MỚI

        val btnChangeAvatar = view.findViewById<ImageButton>(R.id.btn_change_avatar)
        val btnUpdateProfile = view.findViewById<Button>(R.id.btn_update_profile)

        // CÓ THỂ LOAD THÔNG TIN CŨ TẠI ĐÂY NẾU CẦN
        // loadTeacherInfo()

        btnChangeAvatar.setOnClickListener {
            openImageChooser()
        }

        btnUpdateProfile.setOnClickListener {
            saveProfileInfo()
        }
    }

    private fun openImageChooser() {
        // Intent để mở thư viện ảnh
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageChooserLauncher.launch(intent)
    }

    private fun saveProfileInfo() {
        val name = etTeacherName.text.toString().trim()
        val email = etTeacherEmail.text.toString().trim()

        // LẤY GIÁ TRỊ CÁC TRƯỜNG MỚI
        val faculty = etTeacherFaculty.text.toString().trim()
        val title = etTeacherTitle.text.toString().trim()
        val phone = etTeacherPhone.text.toString().trim()
        // KẾT THÚC LẤY GIÁ TRỊ MỚI

        if (name.isEmpty() || email.isEmpty() || faculty.isEmpty() || title.isEmpty() || phone.isEmpty()) {
            Toast.makeText(context, "Vui lòng điền đầy đủ các thông tin cá nhân", Toast.LENGTH_SHORT).show()
            return
        }

        // Logic lưu thông tin vào DB/API...
        // Tại đây bạn sẽ gửi các giá trị (name, email, faculty, title, phone) lên server.
        Toast.makeText(context, "Đã cập nhật thông tin Giảng viên thành công!", Toast.LENGTH_SHORT).show()
    }
}