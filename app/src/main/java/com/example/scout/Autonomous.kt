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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Autonomous(scoutingViewModel: ScoutingViewModel, navController: NavHostController) {
    // Initialize state for each input field, one per data field
    val fieldValues = remember { mutableStateOf(mapOf<String, String>()) }

    // Observe the fields for the Autonomous section
    val fieldsForAutonomous by scoutingViewModel.fieldsForAutonomous.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
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
            Text(text = "Autonomous Period", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            // Iterate over the fields for the autonomous section and create number input fields
            fieldsForAutonomous.forEach { field ->
                val fieldName = field.fieldName
                val currentValue = fieldValues.value[fieldName] ?: ""

                // Render a number input for each field
                OutlinedTextField(
                    value = currentValue,
                    onValueChange = { newValue ->
                        // Ensure only numbers are entered
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            fieldValues.value += (fieldName to newValue)
                        }
                    },
                    label = { Text(fieldName) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            Row {
                Button(
                    onClick = {
                        navController.navigate("home")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        // Add fields to database or continue to next screen
                        navController.navigate("teleop")
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
fun AutonomousPreview() {
    ScoutTheme {
        val navController = TestNavHostController(LocalContext.current)
        Autonomous(ScoutingViewModel(ScoutingRepository()), navController)
    }
}*/
