package com.example.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingInputFields
import com.example.scout.ui.theme.Cream
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.viewmodels.ScoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDataField(scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val fieldToDelete by scoutingViewModel.selectedField.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val fieldName = scoutingViewModel.fieldToDelete
    val section = fieldToDelete?.section
    val inputType = fieldToDelete?.fieldInputType


    LaunchedEffect(scoutingViewModel.fieldToDelete) {
        scoutingViewModel.fieldToDelete?.let { scoutingViewModel.fetchFieldByFieldName(it) }
    }

    CenterAlignedTopAppBar(
        title = { Text(text = "9181 PlatyPirates", style = MaterialTheme.typography.headlineSmall) }
    )
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (fieldName != null && section != null && inputType != null) {
            Text(text = fieldName, color = PlatyRed, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(text = section, style = MaterialTheme.typography.headlineMedium)
            Text(text = inputType, style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.height(30.dp))
        if(showDialog.value){
            fieldToDelete?.let { DrawConfirmDeleteFieldNotification(it, scoutingViewModel, showDialog, navController) }
        }

        Row {
            Button(onClick = {
                navController.navigate("removeDataFieldMenu")
            },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Cancel")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                showDialog.value = true
            },
                modifier = Modifier.width(200.dp)
            ) {
                Text(text = "Remove")
            }
        }

    }
}

@Composable
fun DrawConfirmDeleteFieldNotification(fieldToDelete: ScoutingInputFields, scoutingViewModel: ScoutingViewModel, showDialog: MutableState<Boolean>, navController: NavHostController){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm Deletion")},
        text = { Text(text = "Please press confirm to permanently delete this input field. This cannot be undone!", color = Color.White)},
        confirmButton = {
            TextButton(
                onClick = {
                    fieldToDelete.let {
                        scoutingViewModel.deleteFieldFromScoutingInputFields(it)
                    }
                    navController.navigate("removeDataFieldMenu")
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

fun deleteFieldFromScoutingInputFields(
    scoutingViewModel: ScoutingViewModel,
    section: String,
    fieldName: String,
    fieldInputType: String,
    dropdownOptions: String?
) {
    scoutingViewModel.deleteFieldFromScoutingInputFields(
        ScoutingInputFields(
            section = section,
            fieldName = fieldName,
            fieldInputType = fieldInputType,
            dropdownOptions = dropdownOptions
        )
    )

}

/*@Preview(showBackground = true)
@Composable
fun RemoveDataFieldPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        RemoveDataField(navController)
    }
}*/

