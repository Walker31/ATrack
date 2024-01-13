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
import com.example.atrack.ui.item.toItemUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AttendanceViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository
) : ViewModel() {

    private var itemUiState by mutableStateOf(ItemUiState())
    private val itemId: Int = checkNotNull(savedStateHandle[AttendanceDestination.itemIdArg])
    private val itemDetails = ItemDetails()
    private val itemDetails1 = ItemDetails1()


    init {
        initializeDate(itemDetails1, itemDetails)
        viewModelScope.launch {
            try {
                val data = itemsRepository.getItemStream(itemId)
                    .filterNotNull()
                    .first()
                println("Received data: $data")

                itemUiState = data.toItemUiState(true)
                itemDetails1.id = itemUiState.itemDetails.id
                itemDetails1.subName = itemUiState.itemDetails.subName
                itemDetails1.subCode = itemUiState.itemDetails.subCode
                println(itemUiState)
                println(itemDetails1)
            }
            catch (e: Exception) {
                // Log any exceptions that might occur during data retrieval
                println("Error retrieving data: $e")
            }
        }
    }

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


    suspend fun updateAttendance(itemDetails1: ItemDetails1){
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

    private fun initializeDate(itemDetails1: ItemDetails1, itemDetails: ItemDetails) {
        itemDetails1.id = itemDetails.id
        itemDetails1.subCode = itemDetails.subCode
        itemDetails1.subName = itemDetails.subName
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
