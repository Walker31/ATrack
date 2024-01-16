package com.example.atrack.ui.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atrack.R
import com.example.atrack.TrackTopAppBar
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.ui.AppViewModelProvider
import com.example.atrack.ui.navigation.NavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


object SearchDestination : NavigationDestination {
    override val route = "Search"
    override val titleRes = R.string.app_name
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedDate = remember { mutableStateOf("") }

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
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchUI(
    modifier: Modifier = Modifier,
    selectedDate: MutableState<String>,
    viewModel: SearchViewModel =viewModel(factory = AppViewModelProvider.Factory)
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
            Text("Search in Progress......",modifier = Modifier.padding(16.dp))
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
        items(items=itemList,key= {it.date }) {
                item ->
            SearchItem(item = item,
                modifier= modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
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
                val formattedDate="$dayOfMonth/${month+1}/$year"
                selectedDate.value = formattedDate

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.value?.show()
    }
        OutlinedTextField(
            value = selectedDate.value,
            onValueChange = {},
            label = { Text(stringResource(R.string.date)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            trailingIcon = {
                IconButton(onClick = {showDatePicker()}) {
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
/*

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchPreview() {
    ATrackTheme(darkTheme = true) {
        SearchBody(listOf(
            AttendanceTrack(1, "Game", "100.0", "0",false),
            AttendanceTrack(2, "Pen", "200.0", "30",true)
        ),
            selectedDate ="0"
        )
    }
}
*/

@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TextPreview() {
    val viewModel: SearchViewModel = viewModel()
    val selectedDate = remember { mutableStateOf("0") }

    SearchUI(
        selectedDate = selectedDate,
        viewModel = viewModel
    )
}


