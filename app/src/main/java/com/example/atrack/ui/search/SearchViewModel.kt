package com.example.atrack.ui.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.data.SubjectsRepository
import com.example.atrack.ui.attendance.AttendanceDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: SubjectsRepository
) : ViewModel() {

    private val _searchResult = mutableStateOf<List<AttendanceTrack>?>(null)
    val searchResult: State<List<AttendanceTrack>?> = _searchResult

    private val itemId: String = checkNotNull(savedStateHandle[AttendanceDestination.itemIdArg])

    private var _searchUiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> get() = _searchUiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            itemsRepository.getAllItemsOnDateStream(itemId).collect{ items: List<AttendanceTrack>->
                _searchUiState.value = SearchUiState(itemList = items)
            }
        }
    }

    fun clearData() {
        _searchResult.value = null
    }


    fun searchResult(date: String): List<AttendanceTrack> {

        _searchResult.value = itemsRepository.getAllItemsOnDate(date)
        return _searchResult.value!!
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SearchUiState(
    val itemList: List<AttendanceTrack> = listOf()
)