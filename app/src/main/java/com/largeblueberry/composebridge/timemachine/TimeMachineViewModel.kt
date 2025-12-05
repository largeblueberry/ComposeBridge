package com.largeblueberry.composebridge.timemachine

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimeMachineViewModel @Inject constructor(
    private val repository: TimeMachineRepository // Repository 주입
) : ViewModel() {

    // Repository에서 데이터를 가져와 UI에 노출합니다.
    val timeCapsules: StateFlow<List<TimeCapsule>> = repository.getAllCapsules()

    // CartViewModel이 TimeMachineViewModel을 알 필요가 없으므로,
    // createTimeCapsuleFromCart 함수는 제거하거나, UseCase를 통해 간접적으로 호출되도록 합니다.
    // 여기서는 UI에 필요한 기능만 남깁니다.
}
