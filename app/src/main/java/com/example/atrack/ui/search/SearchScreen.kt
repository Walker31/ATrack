package com.example.atrack.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atrack.R
import com.example.atrack.TrackTopAppBar
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.ui.AppViewModelProvider
import com.example.atrack.ui.navigation.NavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

object SearchDestination : NavigationDestination {
    override val route = "Search"
    override val titleRes = R.string.app_name
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedDate = remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearData()
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrackTopAppBar(
                title = stringResource(SearchDestination.titleRes),
                navigateUp = onNavigateUp,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior
            )
        }
    )
    { innerPadding ->
        SearchUI(
            modifier = modifier
                .padding(innerPadding),
            selectedDate = selectedDate,
            viewModel = viewModel
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchUI(
    modifier: Modifier = Modifier,
    selectedDate: MutableState<String>,
    viewModel: SearchViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    )
    {
        val search = datePickerTextField(selectedDate = selectedDate)

        var searchResult: List<AttendanceTrack>? by remember { mutableStateOf(null) }

        ElevatedButton(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    searchResult = viewModel.searchResult(search)
                }
            },
            shape = MaterialTheme.shapes.large,
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text("Get")
        }

        if (searchResult != null) {
            SearchBody(itemList = searchResult!!)
        } else {
            Text("Search in Progress......", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun SearchBody(
    itemList: List<AttendanceTrack>, modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (itemList.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.sad_face),
                contentDescription = "Sad Face",
                modifier = modifier
                    .size(200.dp)
            )
            Text(
                text = stringResource(R.string.no_class),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        else {
            Column{
                SearchList(
                    itemList = itemList,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
fun SearchList(itemList: List<AttendanceTrack>, modifier: Modifier = Modifier)
{
    LazyColumn(modifier = modifier) {
        items(items=itemList) {
                item ->
            key(item.date) {
                SearchItem(
                    item = item,
                    modifier = modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
fun SearchItem(item: AttendanceTrack, modifier: Modifier = Modifier){
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.subName,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = if (item.attendance) "Present" else "Absent",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun datePickerTextField(
    selectedDate: MutableState<String>
): String {
    val datePickerDialog = remember { mutableStateOf<DatePickerDialog?>(null) }
    val context = LocalContext.current

    fun showDatePicker() {
        val calendar = Calendar.getInstance()

        datePickerDialog.value = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = "$dayOfMonth/${month + 1}/$year"
                selectedDate.value = formattedDate // Only update when new date is selected
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.value?.datePicker?.maxDate = System.currentTimeMillis()

        datePickerDialog.value?.show()
    }

    OutlinedTextField(
        value = selectedDate.value,
        onValueChange = {}, // Remove the onValueChange callback
        label = { Text(stringResource(R.string.date)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        trailingIcon = {
            IconButton(onClick = { showDatePicker() }) {
                Icon(
                    Icons.Rounded.DateRange,
                    contentDescription = "Localized description"
                )
            }
        },
        singleLine = true
    )
    return selectedDate.value
}