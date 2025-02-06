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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingReport
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Endgame(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val fieldValues = remember { mutableStateOf(mapOf<String, String>()) }
    val fieldsForEndgame by scoutingViewModel.fieldsForEndgame.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    val showDialog = remember { mutableStateOf(false)}

    LaunchedEffect(Unit) {
        scoutingViewModel.loadFieldsForEndgame()
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
            Text(text = "Endgame Period", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            fieldsForEndgame.forEach { field ->
                OutlinedTextField(
                    value = fieldValues.value[field.fieldName]?:"",
                    onValueChange = { newValue ->
                        fieldValues.value = fieldValues.value.toMutableMap().apply{
                            put(field.fieldName, newValue)
                        }
                    },// Handle value changes
                    label = { Text(field.fieldName) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide()}
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            if(showDialog.value){
                DrawConfirmNotification(showDialog)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    onClick = {
                        navController.navigate("teleop")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        fieldsForEndgame.forEach { field ->
                            val userInput = fieldValues.value[field.fieldName]?:""
                            addReportToDatabase(
                                scoutingViewModel,
                                scoutingViewModel.reportId,
                                teamViewModel,
                                "Endgame",
                                field.fieldName,
                                userInput
                            )
                        }
                        showDialog.value = true
                        navController.navigate("exportReport")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Composable
fun DrawConfirmNotification(showDialog: MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm Submission")},
        text = { Text(text = "Please press confirm to finalize all fields")},
        confirmButton = {
            Button(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text(
                    text = "Confirm"
                )
            }

        }
    )
}

fun addReportToDatabase(
    scoutingViewModel: ScoutingViewModel,
    reportId: Int,
    teamViewModel: TeamViewModel,
    section: String,
    fieldName: String,
    enteredValue: String
    ){
    scoutingViewModel.addScoutingReport(
        ScoutingReport(
            reportId = reportId,
            teamNumberBeingScouted = teamViewModel.teamNumberBeingScouted,
            gameplaySection = section,
            fieldName = fieldName,
            enteredValue = enteredValue
        )
    )
}
/*@Preview(showBackground = true)
@Composable
fun EndgamePreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        Teleop(navController)
    }
}*/




