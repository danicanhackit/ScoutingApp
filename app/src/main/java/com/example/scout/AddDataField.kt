package com.example.scout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingInputFields
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.Cream
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.viewmodels.ScoutingViewModel
import com.example.scout.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataField(teamViewModel: TeamViewModel, scoutingViewModel: ScoutingViewModel, navController: NavHostController){
    val keyboardController = LocalSoftwareKeyboardController.current

    // dropdown variables
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val fieldTypeOptions = listOf("Number", "Dropdown")
    val itemPosition = remember { mutableStateOf(0)}

    // adding dropdown options
    val addDropdownOptions = remember { mutableStateListOf<String>() }
    var newOption by remember { mutableStateOf(TextFieldValue("")) }

    //database variables
    val sectionToAddFieldTo = teamViewModel.dataFieldSection
    var fieldInputTypeToAdd by remember { mutableStateOf("") }
    var fieldName by remember { mutableStateOf(TextFieldValue("")) }

    var showDialog = remember { mutableStateOf(false)}

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

        Text(text = "Add New "+teamViewModel.dataFieldSection+" Field",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center)

        OutlinedTextField(
            value = fieldName,
            onValueChange = { fieldName = it },
            label = { Text("Name of Field:", color = Burgundy) },
            textStyle = TextStyle(color = Burgundy),
            // keyboard controller code from that one resource i put in a google doc
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide()}
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PlatyRed,
                unfocusedBorderColor = Burgundy,
                cursorColor = Burgundy
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = fieldTypeOptions[itemPosition.value])
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                fieldTypeOptions.forEachIndexed { index, fieldTypeOption ->
                    DropdownMenuItem(text = {
                        Text(text = fieldTypeOption)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                            fieldInputTypeToAdd = fieldTypeOption
                        })
                }
            }
            }
        Spacer(modifier = Modifier.height(50.dp))

        if(fieldInputTypeToAdd == "Dropdown"){
            Column (
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Add Dropdown Options")
                // Input field to add new dropdown options
                OutlinedTextField(
                    value = newOption,
                    onValueChange = { newOption = it },
                    label = { Text("New Option", color = Burgundy) },
                    textStyle = TextStyle(color = Burgundy),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PlatyRed,
                        unfocusedBorderColor = Burgundy,
                        cursorColor = Burgundy
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Button to add option
                Button(onClick = {
                    if (newOption.text.isNotBlank()) {
                        addDropdownOptions.add(newOption.text)
                        newOption = TextFieldValue("") // Clear input after adding
                    }
                }) {
                    Text("Add Option")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Display added options
                Column {
                    addDropdownOptions.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = option, modifier = Modifier.weight(1f))
                            Button(onClick = { addDropdownOptions.removeAt(index) }) {
                                Text("Remove")
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

        if(showDialog.value){
            if (sectionToAddFieldTo != null) {
                DrawConfirmAddFieldNotification(
                    sectionToAddFieldTo,
                    scoutingViewModel,
                    fieldName,
                    fieldInputTypeToAdd,
                    addDropdownOptions,
                    showDialog,
                    navController)
            }
        }

        Row{
            Button(onClick = {
                navController.navigate("addDataFieldMenu")
            },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Cancel")
            }

            Spacer(modifier = Modifier.width(30.dp))

            Button(onClick = {
                showDialog.value = true
                //navController.navigate("addDataFieldMenu")

               /* if(sectionToAddFieldTo != null) {
                    addDataFieldToDatabase(
                        scoutingViewModel,
                        sectionToAddFieldTo,
                        fieldName.text,
                        fieldInputTypeToAdd,
                        addDropdownOptions
                    )
                }*/
            },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Confirm")
            }
        }
    }
}

@Composable
fun DrawConfirmAddFieldNotification(
    sectionToAddFieldTo: String,
    scoutingViewModel: ScoutingViewModel,
    fieldName: TextFieldValue,
    fieldInputTypeToAdd: String,
    addDropdownOptions: List<String>,
    showDialog: MutableState<Boolean>,
    navController: NavHostController
){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm New Data Field")},
        text = { Text(text = "Please press confirm to add this input data field. This field can be deleted later if desired.", color = Color.White)},
        confirmButton = {
            TextButton(
                onClick = {
                    addDataFieldToDatabase(
                        scoutingViewModel,
                        sectionToAddFieldTo,
                        fieldName.text,
                        fieldInputTypeToAdd,
                        addDropdownOptions
                    )
                    navController.navigate("addDataFieldMenu")
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

fun addDataFieldToDatabase(
    scoutingViewModel: ScoutingViewModel,
    section: String,
    fieldName: String,
    fieldInputType: String,
    dropdownOptions: List<String>
    ) {
    scoutingViewModel.insertFieldToScoutingInputFields(
        ScoutingInputFields(
            section = section,
            fieldName = fieldName,
            fieldInputType = fieldInputType,
            dropdownOptions = dropdownOptions.joinToString(","))
    )
}


/*@Preview(showBackground = true)
@Composable
fun AddDataFieldPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        AddDataField(teamViewModel, navController)
    }
}*/

