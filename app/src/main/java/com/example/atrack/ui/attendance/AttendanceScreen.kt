package com.example.atrack.ui.attendance

import android.app.DatePickerDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atrack.R
import com.example.atrack.TrackTopAppBar
import com.example.atrack.data.Subject
import com.example.atrack.ui.AppViewModelProvider
import com.example.atrack.ui.home.HomeDestination
import com.example.atrack.ui.item.ItemDetails
import com.example.atrack.ui.item.ItemDetails1
import com.example.atrack.ui.item.toItem
import com.example.atrack.ui.navigation.NavigationDestination
import com.example.atrack.ui.theme.ATrackTheme
import kotlinx.coroutines.launch
import java.util.Calendar


object AttendanceDestination : NavigationDestination {
    override val route = "submit_attendance"
    override val titleRes = R.string.submit_attendance
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier= Modifier,
    viewModel: AttendanceViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    val selectedDate = remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrackTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        AttendanceTile(
            itemDetailsUiState = uiState.value,
            onAddClick = {
                val item=uiState.value.itemDetails.toItem()

                coroutineScope.launch{
                    viewModel.updateAttendance(
                        itemDetails1 = ItemDetails1(
                        item.id,
                        item.subName,
                        item.subCode,
                        selectedDate.value,
                        true
                        )
                    )
                    println("Updated ItemDetails: $item")
                    navigateBack()
                }

            },
            onAddClick1 = {
                val item=uiState.value.itemDetails.toItem()
                coroutineScope.launch{
                    viewModel.updateAttendance(itemDetails1 = ItemDetails1(
                        id = item.id,
                        subName = item.subName,
                        subCode = item.subCode,
                        date = selectedDate.value,
                        attendance = false))
                    navigateBack()
                          }
            },
            selectedDate = selectedDate,
            modifier = modifier
                .padding(innerPadding)
        )
    }
}


@Composable
fun AttendanceTile(
    itemDetailsUiState: AttendanceUiState,
    selectedDate: MutableState<String>,
    onAddClick: () -> Unit,
    onAddClick1: ()-> Unit,
    modifier: Modifier=Modifier
){

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ,        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {

        TileDetails(
            item = itemDetailsUiState.itemDetails.toItem(),
            selectedDate=selectedDate,
            modifier = Modifier.fillMaxWidth()
        )

        Row{
            Button(
                onClick = onAddClick,
                shape = MaterialTheme.shapes.small,
                enabled = itemDetailsUiState.isEntryValid,
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .weight(1f)
            ) {
                Text("Present")
            }

            ElevatedButton(
                onClick = onAddClick1,
                shape = MaterialTheme.shapes.small,
                enabled = itemDetailsUiState.isEntryValid,
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .weight(1f)
            ) {
                Text("Absent")
            }
        }
    }
}

@Composable
fun TileDetails(
    item: Subject,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedDate: MutableState<String>,
    onValueChange: (ItemDetails1) -> Unit = {}
){
Card(
modifier = modifier,
    colors = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer )
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemDetailsRow(
            labelResID = R.string.SubName,
            itemDetail = item.subName,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen
                        .padding_small)
            )
        )
        ItemDetailsRow(
            labelResID = R.string.SubCode,
            itemDetail = item.subCode,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen
                        .padding_small)
            )
        )
        ItemDetailsRow(
            labelResID = R.string.nTotal,
            itemDetail = item.nPresent.toString(),
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen
                        .padding_small)
            )
        )
    }
    Column(
        modifier=modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        DatePickerTextField(selectedDate,enabled)
    }
}
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DatePickerTextField(
    selectedDate: MutableState<String>,
    enabled: Boolean = true,
) {

    val datePickerDialog = remember { mutableStateOf<DatePickerDialog?>(null) }
    val context = LocalContext.current

    fun showDatePicker() {
        val calendar = Calendar.getInstance()

        datePickerDialog.value = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Set the selected date in the text field
                selectedDate.value = "$dayOfMonth/${month+1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.value?.show()
    }


    Column {
        OutlinedTextField(
            value = selectedDate.value,
            onValueChange = { },
            label = { Text(stringResource(R.string.date)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            trailingIcon = {
                IconButton(onClick = {showDatePicker()}) {
                    Icon(
                        Icons.Rounded.DateRange,
                        contentDescription = "Localized description"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AttendancePreview() {
    ATrackTheme(darkTheme = true) {
        AttendanceTile(AttendanceUiState(
            itemDetails = ItemDetails(1, "Electronics", "EEPC10", "10","4"),
            itemDetails1 = ItemDetails1( 1,"Electronics", "EEPC10","14/23/42",true),
            true
        ),
            onAddClick ={}, onAddClick1 ={}, selectedDate =remember { mutableStateOf("") } )
    }
}
