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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Teleop(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val fieldValues = remember { mutableStateOf(mapOf<String, String>()) }
    val fieldsForTeleop by scoutingViewModel.fieldsForTeleop.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    val reportSectionsToDelete by scoutingViewModel.reportsBySection.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        scoutingViewModel.loadFieldsForTeleop()
        scoutingViewModel.getReportFieldsBySection(scoutingViewModel.reportId, "Autonomous")
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
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Teleop Period", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            // testing scrolling
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ){
                fieldsForTeleop.forEach { field ->
                    if(field.fieldInputType == "Number"){
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
                    } else if(field.fieldInputType == "Dropdown"){
                        val selectedValue = fieldValues.value[field.fieldName] // Can be null
                        DrawDropdownOptions(field, selectedValue) { selectedOption ->
                            fieldValues.value = fieldValues.value.toMutableMap().apply {
                                put(field.fieldName, selectedOption)
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    onClick = {
                        reportSectionsToDelete.forEach { reportSection ->
                            scoutingViewModel.deleteScoutingReport(reportSection)
                        }
                        navController.navigate("autonomous")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        fieldsForTeleop.forEach { field ->
                            val userInput = fieldValues.value[field.fieldName]?:""
                            addReportToDatabase(
                                scoutingViewModel,
                                scoutingViewModel.reportId,
                                teamViewModel,
                                "Teleop",
                                field.fieldName,
                                userInput
                            )
                        }
                        navController.navigate("endgame")
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
fun TeleopPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        Teleop(navController)
    }
}*/


