package com.example.atrack.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.atrack.ui.attendance.AttendanceDestination
import com.example.atrack.ui.attendance.AttendanceScreen
import com.example.atrack.ui.home.HomeDestination
import com.example.atrack.ui.home.HomeScreen
import com.example.atrack.ui.item.ItemDetailsDestination
import com.example.atrack.ui.item.ItemDetailsScreen
import com.example.atrack.ui.item.ItemEditDestination
import com.example.atrack.ui.item.ItemEditScreen
import com.example.atrack.ui.item.ItemEntryDestination
import com.example.atrack.ui.item.ItemEntryScreen
@Composable
fun TrackNavHost(navController: NavHostController,
                  modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateToAddAttendance = { navController.navigate("${AttendanceDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = AttendanceDestination.routeWithArgs,
            arguments = listOf(navArgument(AttendanceDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            AttendanceScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}