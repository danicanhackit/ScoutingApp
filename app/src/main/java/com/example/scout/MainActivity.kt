package com.example.scout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scout.database.DatabaseProvider
import com.example.scout.database.ScoutingRepository
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.ScoutingViewModelFactory
import com.example.scout.viewmodels.TeamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoutTheme {
                val navController = rememberNavController()
                val database = DatabaseProvider.getDatabase(applicationContext)
                val scoutingRepository = ScoutingRepository(database.scoutingInputFieldsDao(), database.scoutingReportDao())
                val scoutingViewModel: ScoutingViewModel by viewModels{
                    ScoutingViewModelFactory(scoutingRepository)
                }
                AppNavigation(navController, scoutingViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, scoutingViewModel: ScoutingViewModel) {
    val teamViewModel: TeamViewModel = viewModel()

    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") { SignIn(teamViewModel, navController) }
        composable("start/{scouterName}/{teamNickname}") { backStackEntry ->
            val scouterName = backStackEntry.arguments?.getString("scouterName") ?: ""
            val teamNickname = backStackEntry.arguments?.getString("teamNickname") ?: ""
            StartScreen(scouterName, teamNickname, navController)
        }
        composable("editDataFields") { EditDataFields(navController)}
        composable("removeDataFieldMenu") { RemoveDataFieldMenu(navController)}
        composable("addDataFieldMenu") { AddDataFieldMenu(teamViewModel, navController)}
        composable("addDataField") { AddDataField(teamViewModel, scoutingViewModel, navController)}

        composable("eventSelection") { EventSelection(teamViewModel, navController)}
        composable("home") { Home(teamViewModel, navController)}

        composable("addReport"){ AddReport(teamViewModel, scoutingViewModel, navController)}
        composable("autonomous") { Autonomous(teamViewModel, scoutingViewModel, navController)}
        composable("teleop") { Teleop(teamViewModel, scoutingViewModel, navController)}
        composable("endgame") { Endgame(teamViewModel, scoutingViewModel, navController)}
        composable("exportReport") { ExportReport(teamViewModel, scoutingViewModel, navController)}
    }
}




