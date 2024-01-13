package com.example.atrack.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.data.SubjectsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    itemsRepository: SubjectsRepository
) : ViewModel()  {
    val historyUiState: StateFlow<HistoryUiState> =
        itemsRepository.getHistory().map { HistoryUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HistoryUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HistoryUiState(val itemList: List<AttendanceTrack> = listOf())