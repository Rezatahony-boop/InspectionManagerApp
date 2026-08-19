package ir.inspectionmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.inspectionmanager.presentation.screen.AddInspectionScreen
import ir.inspectionmanager.presentation.screen.ArchiveScreen
import ir.inspectionmanager.presentation.screen.EditInspectionScreen
import ir.inspectionmanager.presentation.screen.HomeScreen
import ir.inspectionmanager.presentation.screen.ReportsScreen
import ir.inspectionmanager.presentation.screen.SettingsScreen
import ir.inspectionmanager.presentation.viewmodel.InspectionViewModel

@Composable
fun AppNavigation(viewModel: InspectionViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddInspection = {
                    navController.navigate("add_inspection")
                },
                onNavigateToArchive = {
                    navController.navigate("archive")
                },
                onNavigateToReports = {
                    navController.navigate("reports")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("add_inspection") {
            AddInspectionScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("archive") {
            ArchiveScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { inspectionId ->
                    navController.navigate("edit_inspection/$inspectionId")
                }
            )
        }

        composable("reports") {
            ReportsScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("edit_inspection/{inspectionId}") { backStackEntry ->

            val inspectionId =
                backStackEntry.arguments
                    ?.getString("inspectionId")
                    ?.toLongOrNull()

            if (inspectionId != null) {
                EditInspectionScreen(
                    viewModel = viewModel,
                    inspectionId = inspectionId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
