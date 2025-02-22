package com.example.scout

import android.util.Log
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingReport
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.Cream
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Endgame(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val fieldValues = remember { mutableStateOf(mapOf<String, String>()) }
    val fieldsForEndgame by scoutingViewModel.fieldsForEndgame.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    val showDialog = remember { mutableStateOf(false)}
    val reportSectionsToDelete by scoutingViewModel.reportsBySection.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        scoutingViewModel.loadFieldsForEndgame()
        scoutingViewModel.getReportFieldsBySection(scoutingViewModel.reportId, "Teleop")
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
                val textState = remember {mutableStateOf("")}
                OutlinedTextField(
                    value = textState.value,
                    onValueChange = { newValue ->
                        textState.value = newValue
                        fieldValues.value = fieldValues.value.toMutableMap().apply{
                            put(field.fieldName, newValue)
                        }
                    },// Handle value changes
                    label = { Text(field.fieldName) },
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
            }

            if(showDialog.value){
                DrawConfirmNotification(showDialog, navController)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    onClick = {
                        reportSectionsToDelete.forEach { reportSection ->
                            scoutingViewModel.deleteScoutingReport(reportSection)
                        }
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
                    },
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Composable
fun DrawConfirmNotification(showDialog: MutableState<Boolean>, navController: NavHostController){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm Submission")},
        text = { Text(text = "Please press confirm to finalize all fields", color = Color.White)},
        confirmButton = {
            TextButton(
                onClick = {
                    //showDialog.value = false
                    navController.navigate("exportReport")
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Cream
                )
            ) {
                Text("Confirm")
            }

        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Cream
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

fun addReportToDatabase(
    scoutingViewModel: ScoutingViewModel,
    reportId: String,
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




