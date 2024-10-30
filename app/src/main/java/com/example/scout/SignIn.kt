package com.example.scout

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.api.BlueAllianceAPI
import com.example.scout.api.RetrofitInstance
import com.example.scout.api.TeamResponse
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.ui.theme.ScoutTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Temporary hardcoded API key for testing purposes
private const val API_KEY = "lYGojKcODnYEUfpa486Fs0Z8oYI9R2TkS3RS6m3qc39PS43SOB3MxVwS2OZtB7Mf"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(teamViewModel: TeamViewModel, navController: NavHostController) {
    var scouterName by remember { mutableStateOf(TextFieldValue("")) }
    var teamNumber by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    //val teamViewModel: TeamViewModel = viewModel()

    Column(modifier = Modifier.fillMaxSize()) {
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
            Text(text = "Welcome!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = teamNumber,
                onValueChange = { teamNumber = it },
                label = { Text("Team Number:", color = Burgundy) },
                textStyle = TextStyle(color = Burgundy),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                    cursorColor = Burgundy
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = scouterName,
                onValueChange = { scouterName = it },
                label = { Text("Scouter Name:", color = Burgundy) },
                textStyle = TextStyle(color = Burgundy),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                    cursorColor = Burgundy
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (scouterName.text.isNotEmpty() && teamNumber.text.isNotEmpty()) {
                    fetchTeamInfo(teamNumber.text, context) { success, nickname ->
                        if (success && nickname != null) {
                            teamViewModel.teamNumber = teamNumber.text
                            Toast.makeText(
                                context,
                                "Welcome ${scouterName.text} from \"$nickname\"",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("start/${scouterName.text}/${nickname}")
                        } else {
                            Toast.makeText(context, "Invalid team number. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Submit")
            }

        }
    }
}

fun fetchTeamInfo(teamNumber: String, context: android.content.Context, onResult: (Boolean, String?) -> Unit) {
    val call = RetrofitInstance.api.getTeam(teamNumber, API_KEY) // Include the API key here

    call.enqueue(object : Callback<TeamResponse> {
        override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
            if (response.isSuccessful) {
                val teamInfo = response.body()
                if (teamInfo != null) {
                    Log.d("API", "Team Info: $teamInfo")
                    onResult(true, teamInfo.nickname)  // Pass nickname to the result callback
                } else {
                    Log.e("API", "Response body is null")
                    onResult(false, null)  // No data found
                }
            } else {
                Log.e("API", "Request failed with status: ${response.code()}")
                response.errorBody()?.let { errorBody ->
                    Log.e("API", "Error body: ${errorBody.string()}")
                }
                onResult(false, null)  // Mark failure
            }
        }

        override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
            Log.e("API", "Error: ${t.message}")
            onResult(false, null) // API call failed
        }
    })
}


@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    ScoutTheme {
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        SignIn(teamViewModel, navController)
    }
}



