package com.example.scout

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.api.RetrofitInstance
import com.example.scout.api.TeamResponse
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.TeamViewModel
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
    var year by remember{ mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

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

            /*OutlinedTextField(
                value = teamNumber,
                onValueChange = { teamNumber = it },
                label = { Text("Team Number:", color = Burgundy) },
                textStyle = TextStyle(color = Burgundy),
                // keyboard controller code from that one resource i put in a google doc
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide()}
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Burgundy,
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            */
            OutlinedTextField(
                value = scouterName,
                onValueChange = { scouterName = it },
                label = { Text("Scouter Name:", color = Burgundy) },
                textStyle = TextStyle(color = Burgundy),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide()}
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Burgundy,
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            /*OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year:", color = Burgundy) },
                textStyle = TextStyle(color = Burgundy),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide()}
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Burgundy,
                    focusedBorderColor = PlatyRed,
                    unfocusedBorderColor = Burgundy,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Button to go to next screen, notification only pops up if going to next screen
            Button(onClick = {
                if (scouterName.text.isNotEmpty() && teamNumber.text.isNotEmpty()) {
                    // Calls fetchTeamInfo function using team number entered by user
                    fetchTeamInfo(teamNumber.text, context) { success, nickname -> // Nickname from onSuccess function
                        if (success && nickname != null) {
                            teamViewModel.teamNumber = teamNumber.text
                            teamViewModel.teamNickname = nickname
                            // Toast is a widget library used for pop-up notifications
                            Toast.makeText(
                                context,
                                // Creates pop-up message welcoming the team from "nickname", which is
                                // pulled from API response
                                "Welcome ${scouterName.text} from \"$nickname\"",
                                Toast.LENGTH_SHORT
                            ).show()
                            teamViewModel.year = year.text.toInt()
                            teamViewModel.scouterName = scouterName.toString()

                            navController.navigate("start")
                        } else {
                            Toast.makeText(context, "Invalid team number. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Submit")
            }*/

            Button(onClick = {
                Toast.makeText(
                    context,
                    "Welcome "+scouterName.text+"!",
                    Toast.LENGTH_SHORT
                ).show()
                teamViewModel.scouterName = scouterName.text
                navController.navigate("start")
            }) {
                Text(text = "Submit")
            }


        }
    }
}

// Parameter of teamNumber, which is entered by the user
fun fetchTeamInfo(teamNumber: String, context: android.content.Context, onResult: (Boolean, String?) -> Unit) {
    // Calls getTeam function from BlueAllianceAPI interface that will return team info
    val call = RetrofitInstance.api.getTeam(teamNumber, API_KEY)

    call.enqueue(object : Callback<TeamResponse> {
        // If API response is successful
        override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
            if (response.isSuccessful) {
                val teamInfo = response.body()
                if (teamInfo != null) {
                    Log.d("API", "Team Info: $teamInfo")
                    // Information I wanted was nickname, passes nickname to result callback
                    onResult(true, teamInfo.nickname)
                } else {
                    Log.e("API", "Response body is null")
                    onResult(false, null)
                }
            } else {
                Log.e("API", "Request failed with status: ${response.code()}")
                response.errorBody()?.let { errorBody ->
                    Log.e("API", "Error body: ${errorBody.string()}")
                }
                onResult(false, null)  // Mark failure
            }
        }
        // If API call fails
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



