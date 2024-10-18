package com.example.scout

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.ui.theme.Burgundy
import com.example.scout.ui.theme.PlatyRed
import com.example.scout.ui.theme.ScoutTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(navController: NavHostController) {
    var scouterName by remember { mutableStateOf(TextFieldValue("")) }
    var teamNumber by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
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
            Text(text = "Welcome!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = teamNumber,
                onValueChange = { teamNumber = it },
                label = { Text("Team Number:", color = Burgundy) },
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
                    Toast.makeText(
                        context,
                        "Welcome ${scouterName.text} from Team ${teamNumber.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("home/${scouterName.text}")
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    ScoutTheme {
        val navController = TestNavHostController(LocalContext.current)
        SignIn(navController)
    }
}