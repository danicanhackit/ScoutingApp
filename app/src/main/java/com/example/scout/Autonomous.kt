package com.example.scout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.database.ScoutingRepository
import com.example.scout.ui.theme.ScoutTheme

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
                            fieldValues.value = fieldValues.value + (fieldName to newValue)
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
