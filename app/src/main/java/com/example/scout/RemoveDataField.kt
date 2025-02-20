package com.example.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.scout.database.ScoutingInputFields
import com.example.scout.viewmodels.ScoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDataField(scoutingViewModel: ScoutingViewModel, navController: NavHostController){
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

        Row {
            Button(onClick = {
                navController.navigate("removeDataFieldMenu")
            },
                modifier = Modifier.width(75.dp)
            ) {
                Text(text = "Cancel")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                // delete the field from the database
                // should change this to just parsing for the name of the field and deleting field with that name
                deleteFieldFromScoutingInputFields(
                    scoutingViewModel,
                    "Autonomous",
                    "Hello",
                    "Number",
                    null
                )
                navController.navigate("removeDataFieldMenu")
            },
                modifier = Modifier.width(75.dp)
            ) {
                Text(text = "Remove")
            }
        }

    }
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

