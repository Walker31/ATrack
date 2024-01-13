package com.example.atrack.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.SubjectsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                try {
                    val count = viewModelScope.async { countClasses() }.await()
                    val present=viewModelScope.async { presentClasses()}.await()
                    ItemDetailsUiState(it.toItemDetails(), count,present)
                } catch (e: Exception) {
                    ItemDetailsUiState(it.toItemDetails(), 0,0) // Provide a default count
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    private fun countClasses(): Int {
        return itemsRepository.getDateCount(uiState.value.itemDetails.subName)
    }

    private fun presentClasses(): Int{
        return itemsRepository.getAttendanceCount(uiState.value.itemDetails.subName)
    }

    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val itemDetails: ItemDetails =ItemDetails(),
    val classCount: Int =0,
    val presentClass:Int =0
)