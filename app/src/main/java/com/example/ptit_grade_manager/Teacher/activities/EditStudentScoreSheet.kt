package com.example.ptit_grade_manager.Teacher.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.ptit_grade_manager.R
import com.example.ptit_grade_manager.model.StudentInClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditStudentScoreSheet : BottomSheetDialogFragment() {

    private var student: StudentInClass? = null
    private var gradeComponents: Map<String, Int>? = null
    private val scoreEditTexts = mutableMapOf<String, EditText>()
    private var listener: ScoreUpdateListener? = null

    interface ScoreUpdateListener {
        fun onScoreUpdated()
    }

    fun setScoreUpdateListener(listener: ScoreUpdateListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sheet_edit_score, container, false)

        // Lấy dữ liệu từ arguments
        student = arguments?.getParcelable("student")
        gradeComponents = arguments?.getSerializable("components") as? Map<String, Int>

        val studentNameTv = view.findViewById<TextView>(R.id.tv_student_name_bottom_sheet)
        val scoresLayout = view.findViewById<LinearLayout>(R.id.ll_score_components)
        val updateButton = view.findViewById<Button>(R.id.btn_update_scores)

        studentNameTv.text = "${student?.name} - ${student?.id}"

        // Tự động tạo các ô nhập điểm
        gradeComponents?.keys?.forEach { componentName ->
            val scoreView = inflater.inflate(R.layout.item_score_edit, scoresLayout, false)
            val tvScoreName = scoreView.findViewById<TextView>(R.id.tv_score_name)
            val etScoreValue = scoreView.findViewById<EditText>(R.id.et_score_value)

            tvScoreName.text = componentName
            etScoreValue.setText(student?.scores?.get(componentName)?.toString() ?: "")
            scoreEditTexts[componentName] = etScoreValue
            scoresLayout.addView(scoreView)
        }

        updateButton.setOnClickListener {
            var allScoresValid = true
            scoreEditTexts.forEach { (name, editText) ->
                val score = editText.text.toString().toDoubleOrNull()
                if (score != null && score in 0.0..10.0) {
                    student?.scores?.set(name, score)
                } else if (!editText.text.isNullOrEmpty()) {
                    editText.error = "Điểm không hợp lệ"
                    allScoresValid = false
                }
            }

            if (allScoresValid) {
                listener?.onScoreUpdated() // Báo cho Activity biết để cập nhật UI
                dismiss() // Đóng bottom sheet
            } else {
                Toast.makeText(context, "Vui lòng kiểm tra lại điểm đã nhập", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        fun newInstance(student: StudentInClass, gradeComponents: Map<String, Int>): EditStudentScoreSheet {
            val args = Bundle().apply {
                putParcelable("student", student)
                putSerializable("components", HashMap(gradeComponents))
            }
            return EditStudentScoreSheet().apply {
                arguments = args
            }
        }
    }
}