package com.example.scout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(scouterName: String, teamName: String, navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        CenterAlignedTopAppBar(
            title = { Text(text = "9181 PlatyPirates", style = MaterialTheme.typography.headlineSmall)}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, $scouterName!", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = teamName, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                navController.navigate("eventSelection")
            },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Select Event")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate("displayData")
            },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Past Data")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate("editDataFields")
            },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Edit Data Fields")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        StartScreen(scouterName = "Dani", "PlatyPirates", navController)
    }
}
