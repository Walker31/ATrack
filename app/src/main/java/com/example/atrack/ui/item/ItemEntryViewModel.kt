package com.example.atrack.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.atrack.data.AttendanceTrack
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
    val isEntryValid: Boolean = false,
    val itemDetails1: ItemDetails1 =ItemDetails1()
)

data class ItemDetails(
    val id: Int = 0,
    val subName: String = "",
    val subCode: String = "",
    val nPresent: String = "",
    val nTotal: String = "",
    val percent:String=""
)

data class ItemDetails1(
    var id: Int = 0,
    var subName: String = "Hi",
    var subCode: String = "H",
    val date: String="0/00/0000",
    val attendance: Boolean =false
)

fun ItemDetails.toItem(): Subject = Subject(
    id = id,
    subName = subName,
    subCode = subCode,
    nPresent = nPresent.toIntOrNull() ?: 0,
    nTotal = nTotal.toIntOrNull() ?: 0,
    percent=percent.toIntOrNull() ?:0
)

fun ItemDetails1.toItem1(): AttendanceTrack = AttendanceTrack(
    id = id,
    subName = subName,
    subCode = subCode,
    date = date,
    attendance = attendance
)

fun Subject.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun AttendanceTrack.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails1 = this.toItemDetails1(),
    isEntryValid = isEntryValid
)
fun Subject.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    subName = subName,
    subCode = subCode,
    nPresent = nPresent.toString(),
    nTotal = nTotal.toString(),
    percent =percent.toString()
)

fun AttendanceTrack.toItemDetails1(): ItemDetails1 = ItemDetails1(
    id = id,
    subName = subName,
    subCode = subCode,
    date = date,
    attendance=attendance
)