package com.example.scout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import java.io.File

// need to add top bar with 9181 platy
// center text at top
// add a back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReports(navController: NavHostController) {
    val context = LocalContext.current
    val scoutingReportsDir = File(context.getExternalFilesDir(null), "ScoutingReports")

    // Get the list of CSV files
    val csvFiles = remember { scoutingReportsDir.listFiles()?.filter { it.extension == "csv" } ?: emptyList() }
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
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
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scouting Reports",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    navController.navigate("start")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (csvFiles.isEmpty()) {
                Text(text = "No CSV files found.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(csvFiles) { file ->
                        FileItem(file, context)
                    }
                }
            }
        }
    }
}

@Composable
fun FileItem(file: File, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { openFile(context, file) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { shareFile(context, file) }) {
                Text(text = "Share")
            }
        }
    }
}

fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "com.example.scout.files", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "text/csv")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Open CSV File"))
}

fun shareFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "com.example.scout.files", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share CSV File"))
}
