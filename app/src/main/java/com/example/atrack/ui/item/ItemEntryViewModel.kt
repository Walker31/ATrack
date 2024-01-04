package com.example.atrack.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.atrack.data.Subject
import com.example.atrack.data.SubjectsRepository

class ItemEntryViewModel(private val itemsRepository: SubjectsRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            subName.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val subName: String = "",
    val subCode: String = "",
    val nPresent: String = "",
    val nTotal: String = "",
)

fun ItemDetails.toItem(): Subject = Subject(
    id = id,
    subName = subName,
    subCode = subCode,
    nPresent = nPresent.toIntOrNull() ?: 0,
    nTotal = nTotal.toIntOrNull() ?: 0
)

fun Subject.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Subject.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    subName = subName,
    subCode = subCode,
    nPresent = nPresent.toString(),
    nTotal = nTotal.toString()
)