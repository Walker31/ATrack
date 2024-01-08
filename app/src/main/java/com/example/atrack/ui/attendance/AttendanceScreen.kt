package com.example.atrack.ui.attendance

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.atrack.ui.item.ItemUiState
import com.example.atrack.ui.item.toItem
import com.example.atrack.ui.navigation.NavigationDestination
import com.example.atrack.ui.theme.ATrackTheme
import java.util.Calendar
import java.util.Date

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
            itemDetailsUiState = viewModel.itemUiState,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
fun AttendanceTile(
    itemDetailsUiState: ItemUiState,
    modifier: Modifier=Modifier){

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))){


        TileDetails(
            item = itemDetailsUiState.itemDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.Center) {

            Button(
                onClick = { },
                shape = MaterialTheme.shapes.small,
                modifier=modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text("Present")
            }

            ElevatedButton(
                onClick = { },
                shape = MaterialTheme.shapes.small,
                modifier=modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text("Absent")
            }
        }

    }
}

@Composable
fun TileDetails(
    item: Subject, modifier: Modifier = Modifier
){
Card(
modifier = modifier, colors = CardDefaults.cardColors(
containerColor = MaterialTheme.colorScheme.primaryContainer,
contentColor = MaterialTheme.colorScheme.onPrimaryContainer
)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemDetailsRow(
            labelResID = R.string.SubName,
            itemDetail = item.subName,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )
        ItemDetailsRow(
            labelResID = R.string.SubCode,
            itemDetail = item.subCode,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )
        ItemDetailsRow(
            labelResID = R.string.nTotal,
            itemDetail = item.nPresent.toString(),
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
        )
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

@Suppress("NAME_SHADOWING")
@Composable
fun Calender() {
    var myear by remember { mutableStateOf("")}
    var mmonth by remember { mutableStateOf("")}
    var mday by remember { mutableStateOf("")}

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
            myear="$year"
            mmonth="${month+1}"
            mday="$dayOfMonth"
        }, year, month, day
    )
    Column {

        FilledTonalButton(onClick = { datePickerDialog.show() }) {
            Text(text = "Pick Date")
        }
    }

}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AttendancePreview() {
    ATrackTheme(darkTheme = true) {
        AttendanceTile(
            ItemUiState(
            itemDetails = ItemDetails(1, "Electronics", "EEPC10", "10","4")
        ))}
}
