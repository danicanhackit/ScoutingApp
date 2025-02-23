package com.example.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.ScoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDataFieldMenu(scoutingViewModel: ScoutingViewModel, navController: NavHostController) {
    val fieldNames by scoutingViewModel.allInputFieldNames.observeAsState(emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "9181 PlatyPirates", style = MaterialTheme.typography.headlineSmall) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "All Current Input Fields", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fieldNames.forEach { fieldName ->
                    TextButton(onClick = {
                        scoutingViewModel.fieldToDelete = fieldName
                        navController.navigate("removeDataField")
                    }) {
                        Text(text = fieldName)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("editDataFields") },
                modifier = Modifier.width(200.dp)
            ) {
                Text(text = "Exit")
            }
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun RemoveDataFieldMenuPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        RemoveDataFieldMenu(navController)
    }
}*/

