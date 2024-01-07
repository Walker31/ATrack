package com.example.atrack.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.atrack.ui.theme.ATrackTheme
import java.util.Calendar
import java.util.Date

@Suppress("NAME_SHADOWING")
@Composable
fun Calender() {
    var datePicked by remember { mutableStateOf("1")
    }

    val context = LocalContext.current
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            datePicked = "$dayOfMonth/$month/$year"
        }, year, month, day
    )

    FilledTonalButton(onClick = { datePickerDialog.show() }) {
        Text(text = "Pick Date")
    }
}

@Preview
@Composable
fun CalendarPreview() {
    ATrackTheme { // Replace YourAppTheme with your actual theme
        Calender()
    }
}