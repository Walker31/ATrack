package com.example.atrack.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.data.Subject
import com.example.atrack.data.SubjectsRepository
import com.example.atrack.ui.attendance.AttendanceDestination
import com.example.atrack.ui.item.ItemDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository
) : ViewModel()  {

    private val itemId: String = checkNotNull(savedStateHandle[AttendanceDestination.itemIdArg])
    val historyUiState: StateFlow<HistoryUiState> =
        itemsRepository.getHistory(itemId).map { HistoryUiState(itemList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HistoryUiState()
            )

    fun getHistory(itemDetails: Subject): Flow<List<AttendanceTrack>> {
        return itemsRepository.getHistory(subName = itemDetails.subName)
    }

    suspend fun delete(subName: String, date:String){
        itemsRepository.deleteHistory(subName,date)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HistoryUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val itemList: List<AttendanceTrack> = listOf()
)