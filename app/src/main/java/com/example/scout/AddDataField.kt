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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingInputFields
import com.example.scout.ui.theme.Burgundy
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

    //database variables
    val sectionToAddFieldTo = teamViewModel.dataFieldSection
    var fieldInputTypeToAdd by remember { mutableStateOf("") }
    var fieldName by remember { mutableStateOf(TextFieldValue("")) }

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

        Text(text = "Add Data Field", style = MaterialTheme.typography.headlineLarge)
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
        Button(onClick = {
            navController.navigate("addDataFieldMenu")

            // need to add a cancel button

            if(sectionToAddFieldTo != null) {
                addDataFieldToDatabase(
                    scoutingViewModel,
                    sectionToAddFieldTo,
                    fieldName.text,
                    fieldInputTypeToAdd
                )
            }
        },
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Confirm")
        }
        }
}

fun addDataFieldToDatabase(
    scoutingViewModel: ScoutingViewModel,
    section: String,
    fieldName: String,
    fieldInputType: String
    ) {
    scoutingViewModel.insertFieldToScoutingInputFields(
        ScoutingInputFields(
            section = section,
            fieldName = fieldName,
            fieldInputType = fieldInputType)
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

