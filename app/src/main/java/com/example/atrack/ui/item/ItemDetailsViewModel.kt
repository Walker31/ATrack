package com.example.atrack.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.SubjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext


class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {item ->
                val count = withContext(Dispatchers.IO) {
                    itemsRepository.getDateCount(item.subName)
                }
                val present = withContext(Dispatchers.IO) {
                    itemsRepository.getAttendanceCount(item.subName)
                }
                val percent = if (count != 0) (present.toFloat() / count.toFloat()) * 100f else 0f

                itemsRepository.updateSubject(count,present,percent,item.subName)
                println("Count: $count, Present: $present,Percent: $percent")

                ItemDetailsUiState(item.toItemDetails())

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
        itemsRepository.delete(uiState.value.itemDetails.toItem().subName)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val itemDetails: ItemDetails =ItemDetails()
)