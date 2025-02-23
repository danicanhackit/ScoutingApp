package com.example.scout

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.TeamViewModel
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportReport(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "9181 PlatyPirates",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    onClick = {
                        //export file
                        scoutingViewModel.exportReportById(context, scoutingViewModel.reportId,
                            teamViewModel.teamNumberBeingScouted.toString()
                        )
                        Toast.makeText(context, "CSV Exported!", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(text = "Export")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        navController.navigate("home")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Home")
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun ExportReportPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        AddReport(teamViewModel, navController)
    }
}
*/
