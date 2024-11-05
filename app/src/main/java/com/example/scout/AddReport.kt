package com.example.scout

import android.util.Log
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.api.RetrofitInstance
import com.example.scout.api.TeamResponse
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val API_KEY = "lYGojKcODnYEUfpa486Fs0Z8oYI9R2TkS3RS6m3qc39PS43SOB3MxVwS2OZtB7Mf"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReport(navController: NavHostController){
    var teamNum by remember { mutableStateOf(TextFieldValue(" "))}
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // update for actual event name
            Text(text = "Add Report", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = teamNum,
                onValueChange = { teamNum = it },
                label = { Text("Team Being Scouted", color = Burgundy)},
                textStyle = TextStyle(color = Burgundy),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                    cursorColor = Burgundy
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
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
                        // add fields to database
                        navController.navigate("autonomous")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddReportPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        AddReport(navController)
    }
}

