package com.example.scout

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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


private const val API_KEY = "lYGojKcODnYEUfpa486Fs0Z8oYI9R2TkS3RS6m3qc39PS43SOB3MxVwS2OZtB7Mf"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReport(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    var teamNum by remember { mutableStateOf(TextFieldValue(" "))}
    val keyboardController = LocalSoftwareKeyboardController.current
    val reportSectionsToDelete by scoutingViewModel.reportsByTeamNum.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        scoutingViewModel.getReportsById(scoutingViewModel.reportId)
    }

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
            // update for actual event name
            Text(text = "Add Report", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = teamNum,
                onValueChange = { teamNum = it },
                label = { Text("Team Being Scouted", color = Burgundy)},
                textStyle = TextStyle(color = Burgundy),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide()}
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Burgundy,
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    onClick = {
                        reportSectionsToDelete.forEach { reportSection ->
                            scoutingViewModel.deleteScoutingReport(reportSection)
                        }
                        navController.navigate("home")
                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        val userInput = teamNum.text.trim()
                        teamViewModel.teamNumberBeingScouted = userInput.toIntOrNull() ?: 0
                        // this is going to cause problems if someone wants to change
                        // team being scouted but go back to the same report
                        scoutingViewModel.reportId = scoutingViewModel.generateReportId()
                        navController.navigate("autonomous")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun AddReportPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        AddReport(teamViewModel, navController)
    }
}
*/
