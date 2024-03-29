package com.example.atrack.ui.history

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.example.atrack.ui.attendance.AttendanceUiState
import com.example.atrack.ui.item.DeleteConfirmationDialog
import com.example.atrack.ui.item.toItem
import com.example.atrack.ui.navigation.NavigationDestination
import com.example.atrack.ui.theme.ATrackTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object HistoryDestination : NavigationDestination {
    override val route = "history"
    override val titleRes = R.string.history
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    itemDetailsUiState: AttendanceUiState,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val historyUiState by viewModel.historyUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val item=itemDetailsUiState.itemDetails.toItem()

    var items by remember { mutableStateOf<List<AttendanceTrack>>(emptyList()) }
    
    Text(text = item.subName)

    LaunchedEffect(item) {
        viewModel.getHistory(item).collect {
            items = it
        }
    }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrackTopAppBar(
                title = stringResource(HistoryDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        HistoryBody(
            itemList = historyUiState.itemList,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HistoryBody(
    itemList: List<AttendanceTrack>, modifier: Modifier = Modifier
) {
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
                    .fillMaxHeight()
            )
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        else {
            HistoryList(
                itemList = itemList,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun HistoryList(
    itemList: List<AttendanceTrack>, modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val coroutineScope = rememberCoroutineScope()

    fun delete(subName:String,date: String){
        coroutineScope.launch{
            withContext(Dispatchers.IO) {
                viewModel.delete(
                    subName,date
                )
            }

        }
    }
    LazyColumn(modifier = modifier) {
        items(items=itemList,key= {it.id }) {
                item ->
            HistoryItem(item = item,
                modifier= modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                delete={subName, date ->
                    delete(subName, date)
                }
            )
        }
    }
}

@Composable
private fun HistoryItem(
    item: AttendanceTrack,
    modifier: Modifier = Modifier,
    delete: (String,String) -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
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
                    text = item.date,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = if (item.attendance) "Present" else "Absent",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(0.05f))
                IconButton(onClick = {deleteConfirmationRequired = true   },
                        modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete Attendance Button"

                    )
                }
                if (deleteConfirmationRequired) {

                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            delete(item.subName,item.date)
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryBodyPreview() {
    ATrackTheme(darkTheme = false) {
        HistoryItem(
            AttendanceTrack(1, "Game", "100.0", "20/3/2023",true),
            delete = { subName, date ->println("Deleting: $subName on $date") }
        )

    }
}