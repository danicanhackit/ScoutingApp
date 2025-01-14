package com.example.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataFieldMenu(teamViewModel: TeamViewModel, navController: NavHostController){

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

        Button(onClick = {
            teamViewModel.dataFieldSection = "Autonomous"
            navController.navigate("addDataField")
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "New Autonomous Field")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            teamViewModel.dataFieldSection = "Teleop"
            navController.navigate("addDataField")
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "New Teleop Field")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            teamViewModel.dataFieldSection = "Endgame"
            navController.navigate("addDataField")
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "New Endgame Field")
        }
        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            navController.navigate("editDataFields")
        },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Exit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDataFieldMenuPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        AddDataFieldMenu(teamViewModel, navController)
    }
}

