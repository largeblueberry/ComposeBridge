package com.largeblueberry.composebridge.timemachine

import com.largeblueberry.data.cart.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeMachineRepository @Inject constructor() {

    // 인메모리 데이터 저장소 (실제로는 Room DB나 SharedPreferences가 될 수 있습니다)
    private val _capsules = MutableStateFlow<List<TimeCapsule>>(emptyList())
    val capsules: StateFlow<List<TimeCapsule>> = _capsules.asStateFlow()

    /**
     * 새로운 타임캡슐을 생성하고 저장합니다.
     * 이 함수는 CartViewModel에서 직접 호출되지 않고, UseCase를 통해 호출됩니다.
     */
    fun saveNewCapsule(title: String, items: List<CartItem>) {
        if (items.isEmpty()) return

        val newCapsule = TimeCapsule(
            name = title,
            items = items,
            timestamp = System.currentTimeMillis()
        )

        // 새로운 캡슐을 리스트의 가장 앞에 추가합니다.
        _capsules.value = listOf(newCapsule) + _capsules.value

        println("Repository: Time Capsule Saved - ${newCapsule.name}")
    }

    /**
     * 모든 캡슐을 가져옵니다.
     */
    fun getAllCapsules(): StateFlow<List<TimeCapsule>> = capsules

    /**
     * ID로 특정 캡슐을 가져옵니다.
     * Flow를 반환하여 실시간 업데이트를 지원합니다.
     */
    fun getCapsuleById(capsuleId: String): Flow<TimeCapsule?> {
        return capsules.map { capsuleList ->
            capsuleList.find { it.id == capsuleId }
        }
    }

    /**
     * ID로 특정 캡슐을 즉시 가져옵니다. (동기 버전)
     */
    fun getCapsuleByIdSync(capsuleId: String): TimeCapsule? {
        return _capsules.value.find { it.id == capsuleId }
    }

    /**
     * 캡슐을 삭제합니다.
     */
    fun deleteCapsule(capsuleId: String) {
        _capsules.value = _capsules.value.filter { it.id != capsuleId }
        println("Repository: Time Capsule Deleted - $capsuleId")
    }

    /**
     * 캡슐을 업데이트합니다.
     */
    fun updateCapsule(updatedCapsule: TimeCapsule) {
        _capsules.value = _capsules.value.map { capsule ->
            if (capsule.id == updatedCapsule.id) updatedCapsule else capsule
        }
        println("Repository: Time Capsule Updated - ${updatedCapsule.name}")
    }

    /**
     * 모든 캡슐을 삭제합니다.
     */
    fun clearAllCapsules() {
        _capsules.value = emptyList()
        println("Repository: All Time Capsules Cleared")
    }

    /**
     * 캡슐의 개수를 반환합니다.
     */
    fun getCapsuleCount(): Int = _capsules.value.size
}