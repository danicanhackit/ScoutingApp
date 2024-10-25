package com.example.scout

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.api.RetrofitInstance
import com.example.scout.api.TeamEventResponse
import com.example.scout.ui.theme.ScoutTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "lYGojKcODnYEUfpa486Fs0Z8oYI9R2TkS3RS6m3qc39PS43SOB3MxVwS2OZtB7Mf"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSelection(teamViewModel: TeamViewModel, navController: NavHostController) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableStateOf(0) }
    val teamNumber = teamViewModel.teamNumber
    var options by remember { mutableStateOf(listOf<String>()) } // Options for the dropdown

    // Fetch events when the screen is first displayed
    LaunchedEffect(teamNumber) {
        if (teamNumber != null) {
            fetchTeamEvents(teamNumber, 2024, API_KEY) { success, eventNames ->
                if (success && eventNames != null) {
                    options = eventNames // Update the dropdown options with fetched events
                    if (options.isNotEmpty()) {
                        itemPosition.value = 0 // Set default selected item
                    }
                } else {
                    Log.e("API", "Failed to fetch events")
                }
            }
        }
    }

    CenterAlignedTopAppBar(
        title = { Text(text = "9181 PlatyPirates", style = MaterialTheme.typography.headlineSmall) }
    )
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Event Selection", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = if (options.isNotEmpty()) options[itemPosition.value] else "Select Event")
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(text = {
                        Text(text = option)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text(text = "Confirm")
        }
    }
}

fun fetchTeamEvents(
    teamNumber: String,
    year: Int,
    apiKey: String,
    onResult: (Boolean, List<String>?) -> Unit
) {
    val call = RetrofitInstance.api.getTeamEvents(teamNumber, year, apiKey)

    call.enqueue(object : Callback<List<TeamEventResponse>> {
        override fun onResponse(
            call: Call<List<TeamEventResponse>>,
            response: Response<List<TeamEventResponse>>
        ) {
            if (response.isSuccessful) {
                val eventNames = response.body()?.map { it.name } // Extract event names
                onResult(true, eventNames) // Pass the result to the callback
            } else {
                onResult(false, null) // Handle error with null data
            }
        }

        override fun onFailure(call: Call<List<TeamEventResponse>>, t: Throwable) {
            Log.e("API", "Error: ${t.message}")
            onResult(false, null) // Handle failure with null data
        }
    })
}

@Preview(showBackground = true)
@Composable
fun EventSelectionPreview() {
    ScoutTheme {
        val navController = TestNavHostController(LocalContext.current)
        val teamViewModel = TeamViewModel()
        EventSelection(teamViewModel, navController)
    }
}
