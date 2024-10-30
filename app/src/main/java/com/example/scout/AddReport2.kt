package com.example.scout

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
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
fun AddReport2(navController: NavHostController){
    var field1 by remember { mutableStateOf(TextFieldValue(" "))}
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
            Text(text = "Teleop Period", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = field1,
                onValueChange = { field1 = it },
                label = { Text("Field 1:", color = Burgundy)},
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
                        navController.navigate("addReport1")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        // add fields to database
                        navController.navigate("addReport3")
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
fun AddReport2Preview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        AddReport2(navController)
    }
}


