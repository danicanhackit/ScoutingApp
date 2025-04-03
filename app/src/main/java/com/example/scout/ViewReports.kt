package com.example.scout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.ui.theme.Cream
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.TeamViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReports(navController: NavHostController) {
    val context = LocalContext.current
    val scoutingReportsDir = File(context.getExternalFilesDir(null), "ScoutingReports")
    val csvFiles = remember { mutableStateListOf(*scoutingReportsDir.listFiles()?.filter { it.extension == "csv" }?.toTypedArray() ?: emptyArray()) }

    // Create an ActivityResultLauncher to handle the result of directory selection
    val openDirectoryForResult = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        uri?.let {
            // If the user selected a valid directory, call exportToUsb
            exportToUsb(context, csvFiles, uri)
        }
    }

    Column(
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
            Row{
                Button(
                    onClick = {
                        navController.navigate("start")
                    }
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    // Trigger the directory picker when button is clicked
                    openDirectoryForResult.launch(null)
                }) {
                    Text(text = "Export All to USB")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (csvFiles.isEmpty()) {
                Text(text = "No CSV files found.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(csvFiles) { file ->
                        FileItem(file, context) { deletedFile ->
                            csvFiles.remove(deletedFile)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileItem(file: File, context: Context, onDelete: (File) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { openFile(context, file) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (showDialog.value) {
            DrawConfirmDeleteReportNotification(file, showDialog, onDelete)
        }

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Button(onClick = { shareFile(context, file) }) {
                    Text(text = "Share")
                }
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = { showDialog.value = true }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@Composable
fun DrawConfirmDeleteReportNotification(
    file: File,
    showDialog: MutableState<Boolean>,
    onDelete: (File) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm Delete") },
        text = { Text(text = "Please press confirm to delete this scouting report. This cannot be undone!", color = Color.White) },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteFile(file)
                    onDelete(file)
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(contentColor = Cream)
            ) {
                Text("Confirm")
            }

        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(contentColor = Cream)
            ) {
                Text("Cancel")
            }
        }
    )
}

fun deleteFile(file: File) {
    file.delete()
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

fun exportToUsb(context: Context, csvFiles: MutableList<File>, uri: Uri) {
    val contentResolver = context.contentResolver

    // Get a DocumentFile for the URI, representing the root directory of the selected storage location
    val documentFile = DocumentFile.fromTreeUri(context, uri)

    if (documentFile != null && documentFile.canWrite()) {
        var successCount = 0

        // Loop through the CSV files and copy them to the USB storage
        for (file in csvFiles) {
            // Check if the file already exists in the destination directory on USB
            val existingFile = documentFile.findFile(file.name)

            // If the file exists, overwrite it, otherwise create a new file
            val destFile = if (existingFile != null) {
                // If file exists, return the existing file (overwrite behavior)
                existingFile
            } else {
                // If the file doesn't exist, create it
                documentFile.createFile("text/csv", file.name)
            }

            // If the file creation or retrieval was successful, write the content
            if (destFile != null) {
                try {
                    val outputStream = contentResolver.openOutputStream(destFile.uri)
                    val inputStream = file.inputStream()
                    inputStream.copyTo(outputStream!!)
                    outputStream.close()
                    successCount++
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Failed to copy ${file.name}: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }
        }
        Toast.makeText(context, "Exported $successCount files to USB", Toast.LENGTH_LONG).show()
    }
}



@Preview(showBackground = true)
@Composable
fun ViewReportsPreview() {
    ScoutTheme {
        val navController = TestNavHostController(LocalContext.current)
        ViewReports(navController)
    }
}
