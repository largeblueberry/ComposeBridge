package com.largeblueberry.composebridge.timemachine

import androidx.lifecycle.ViewModel
import com.largeblueberry.data.cart.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimeMachineViewModel @Inject constructor() : ViewModel() {

    private val _timeCapsules = MutableStateFlow<List<TimeCapsule>>(emptyList())
    val timeCapsules: StateFlow<List<TimeCapsule>> = _timeCapsules

    fun createTimeCapsuleFromCart(items: List<CartItem>) {
        if (items.isEmpty()) return

        // 실제 앱에서는 사용자에게 캡슐 이름을 입력받는 다이얼로그를 띄우는 것이 좋습니다.
        val capsuleName = "확정본 #${_timeCapsules.value.size + 1}"

        val newCapsule = TimeCapsule(
            name = capsuleName,
            items = items
        )
        // 새로운 캡슐을 리스트의 가장 앞에 추가합니다.
        _timeCapsules.value = listOf(newCapsule) + _timeCapsules.value
    }
}
