

package com.example.ptit_grade_manager.util

sealed class Resource<out T> {

    // ĐỔI TÊN: 'Success' thành 'Success' (giữ nguyên, nhưng giờ là con của Resource)
    data class Success<out T>(val data: T) : Resource<T>()

    // ĐỔI TÊN: 'Error' thành 'Error' (giữ nguyên, nhưng giờ là con của Resource)
    data class Error(val exception: Exception) : Resource<Nothing>()

    // Bạn cũng có thể thêm trạng thái "Đang tải" nếu muốn
    // object Loading : Resource<Nothing>()
}