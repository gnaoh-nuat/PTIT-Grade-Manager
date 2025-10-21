package com.example.ptit_grade_manager.Teacher.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.Teacher.fragments.TeacherClassListFragment
import com.example.ptit_grade_manager.Teacher.fragments.TeacherProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class TeacherMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sử dụng layout mới chứa BottomNavigationView
        setContentView(R.layout.activity_teacher_main)

        bottomNav = findViewById(R.id.bottom_nav)

        // 1. Khởi tạo Fragment mặc định (Lớp học)
        if (savedInstanceState == null) {
            replaceFragment(TeacherClassListFragment())
        }

        // 2. Thiết lập Listener cho Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // ID từ menu/bottom_nav_teacher_menu.xml
                R.id.nav_classes -> {
                    replaceFragment(TeacherClassListFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(TeacherProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // Thay thế Fragment trong FrameLayout có ID là fragment_container (trong activity_teacher_main.xml)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}