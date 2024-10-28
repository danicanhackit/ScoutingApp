package com.example.scout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scout.ui.theme.ScoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoutTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") { SignIn(navController) }
        composable("start/{scouterName}/{teamNickname}") { backStackEntry ->
            val scouterName = backStackEntry.arguments?.getString("scouterName") ?: ""
            val teamNickname = backStackEntry.arguments?.getString("teamNickname") ?: ""
            StartScreen(scouterName, teamNickname, navController)
        }
        composable("eventSelection") { backStackEntry ->
            val eventSelectionViewModel: TeamViewModel = viewModel(backStackEntry)
            EventSelection(eventSelectionViewModel, navController)
        }
        composable("home") { Home(navController)}
        composable("addReport1") { AddReport1(navController)}
    }
}



