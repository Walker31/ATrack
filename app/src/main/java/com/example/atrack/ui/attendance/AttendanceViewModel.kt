package com.example.atrack.ui.attendance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.SubjectsRepository
import com.example.atrack.ui.item.ItemDetails
import com.example.atrack.ui.item.ItemDetails1
import com.example.atrack.ui.item.ItemUiState
import com.example.atrack.ui.item.toItem1
import com.example.atrack.ui.item.toItemDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AttendanceViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository
) : ViewModel() {

    private var itemUiState by mutableStateOf(ItemUiState())
    private val itemId: Int = checkNotNull(savedStateHandle[AttendanceDestination.itemIdArg])
    private val itemDetails1 = ItemDetails1()

    val uiState: StateFlow<AttendanceUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                AttendanceUiState(itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AttendanceUiState()
            )


    suspend fun updateAttendance(subName: String,subCode:String,date: String,attendance: Boolean){
        itemDetails1.subName=subName
        itemDetails1.subCode=subCode
        itemDetails1.attendance=attendance
        itemDetails1.date=date

        if(validateInput())
        try {
            itemsRepository.insertDate(itemDetails1.toItem1())
            println("Updated ItemDetails: $itemDetails1")
        }catch (e: Exception) {
            println("Error updating attendance: $e")
        }
    }

    private fun validateInput(uiState: ItemDetails1 = itemUiState.itemDetails1): Boolean {
        return with(uiState) {
            date.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class AttendanceUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val itemDetails1: ItemDetails1 =ItemDetails1(),
    val isEntryValid: Boolean = true
)
