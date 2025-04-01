package com.example.scout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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

        requestStoragePermission()

        setContent {
            ScoutTheme {
                val navController = rememberNavController()
                val database = DatabaseProvider.getDatabase(applicationContext)
                val scoutingRepository = ScoutingRepository(
                    database.scoutingInputFieldsDao(),
                    database.scoutingReportDao()
                )
                val scoutingViewModel: ScoutingViewModel by viewModels {
                    ScoutingViewModelFactory(scoutingRepository)
                }
                Log.d("FILE PATH", LocalContext.current.getExternalFilesDir(null).toString())
                AppNavigation(navController, scoutingViewModel)
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            if (granted) {
                Toast.makeText(this, "Storage Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied! USB Export may not work.", Toast.LENGTH_LONG).show()
            }
        }

    private fun requestStoragePermission() {
        if (!hasStoragePermission()) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, scoutingViewModel: ScoutingViewModel) {
    val teamViewModel: TeamViewModel = viewModel()

    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") { SignIn(teamViewModel, navController) }
        composable("start") { StartScreen(teamViewModel, navController) }
        composable("viewReports") { ViewReports(navController) }
        composable("editDataFields") { EditDataFields(navController) }
        composable("removeDataFieldMenu") { RemoveDataFieldMenu(scoutingViewModel, navController) }
        composable("removeDataField") { RemoveDataField(scoutingViewModel, navController) }
        composable("addDataFieldMenu") { AddDataFieldMenu(teamViewModel, navController) }
        composable("addDataField") { AddDataField(teamViewModel, scoutingViewModel, navController) }
        composable("eventSelection") { EventSelection(teamViewModel, navController) }
        composable("home") { Home(teamViewModel, navController) }
        composable("addReport") { AddReport(teamViewModel, scoutingViewModel, navController) }
        composable("autonomous") { Autonomous(teamViewModel, scoutingViewModel, navController) }
        composable("teleop") { Teleop(teamViewModel, scoutingViewModel, navController) }
        composable("endgame") { Endgame(teamViewModel, scoutingViewModel, navController) }
        composable("exportReport") { ExportReport(teamViewModel, scoutingViewModel, navController) }
    }
}
