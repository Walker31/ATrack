package com.example.atrack.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.atrack.TrackApplication
import com.example.atrack.ui.attendance.AttendanceViewModel
import com.example.atrack.ui.history.HistoryViewModel
import com.example.atrack.ui.home.HomeViewModel
import com.example.atrack.ui.item.ItemDetailsViewModel
import com.example.atrack.ui.item.ItemEditViewModel
import com.example.atrack.ui.item.ItemEntryViewModel
import com.example.atrack.ui.search.SearchViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }

        initializer {
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }

        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }

        initializer {
            AttendanceViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository)
        }

        initializer {
            HistoryViewModel(
                inventoryApplication().container.itemsRepository
            )
        }

        initializer{
            SearchViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
                )
        }
    }
}

fun CreationExtras.inventoryApplication(): TrackApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackApplication)