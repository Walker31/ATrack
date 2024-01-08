package com.example.atrack.ui.attendance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.SubjectsRepository
import com.example.atrack.ui.item.ItemDetails
import com.example.atrack.ui.item.ItemDetailsUiState
import com.example.atrack.ui.item.ItemUiState
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
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[AttendanceDestination.itemIdArg])

    val uiState: StateFlow<Any> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                AttendanceUiState(itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(AttendanceViewModel.TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
data class AttendanceUiState(
    val itemDetails: ItemDetails = ItemDetails()
)