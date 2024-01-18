package com.example.atrack.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.atrack.data.Subject
import com.example.atrack.ui.AppViewModelProvider
import com.example.atrack.ui.navigation.NavigationDestination
import com.example.atrack.ui.theme.ATrackTheme


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    navigateToSearchItem:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrackTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = navigateToItemEntry,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "ItemEntryTitle"
                    )
                }

                FloatingActionButton(
                    onClick = navigateToSearchItem,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "ItemSearch"
                    )
                }
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Subject>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
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
                        .size(20.dp)
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
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
        }
}

@Composable
private fun InventoryList(
    itemList: List<Subject>, onItemClick: (Subject) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items=itemList,key= {it.id }) {
            item ->
            InventoryItem(item = item,
                modifier= modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun InventoryItem(
    item: Subject, modifier: Modifier = Modifier
) {
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
                Box{
                    Text(
                        text = item.subName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.percent.toString()+" %",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = item.subCode,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeBodyPreview() {
    ATrackTheme(darkTheme = true) {
        HomeBody(listOf(
            Subject(1, "Game", "100.0", 20,50,0f),
            Subject(2, "Pen", "200.0", 30,40,0f)
        ), onItemClick = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeBodyEmptyListPreview() {
    ATrackTheme(darkTheme = true) {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InventoryItemPreview() {
    ATrackTheme(darkTheme = true) {
        InventoryItem(
            Subject(1, "Game", "100.0", 20,50,0f),
        )
    }
}