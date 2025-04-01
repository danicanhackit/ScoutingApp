package com.example.scout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import com.example.scout.ui.theme.Cream
import com.example.scout.ui.theme.ScoutTheme
import com.example.scout.viewmodels.TeamViewModel
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
    val csvFiles = remember { mutableStateListOf(*scoutingReportsDir.listFiles()?.filter { it.extension == "csv" }?.toTypedArray() ?: emptyArray()) }

    //val csvFiles = remember { scoutingReportsDir.listFiles()?.filter { it.extension == "csv" } ?: emptyList() }
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
            Row{
                Button(
                    onClick = {
                        navController.navigate("start")
                    }
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { exportToUsb(context, csvFiles) }) {
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
    val showDialog = remember { mutableStateOf(false)}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { openFile(context, file) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if(showDialog.value){
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
            Column (
                modifier = Modifier.padding(5.dp)
            ){
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
){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text(text = "Confirm Delete")},
        text = { Text(text = "Please press confirm to delete this scouting report. This cannot be undone!", color = Color.White)},
        confirmButton = {
            TextButton(
                onClick = {
                    deleteFile(file)
                    onDelete(file)
                    //Toast.makeText(LocalContext.current, "File Successfully Deleted!", Toast.LENGTH_LONG).show()
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Cream
                )
            ) {
                Text("Confirm")
            }

        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Cream
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

fun deleteFile(file: File){
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

fun exportToUsb(context: Context, csvFiles: List<File>) {
    val usbPath = detectUsbPath()
    if (usbPath == null) {
        Toast.makeText(context, "No USB drive detected", Toast.LENGTH_LONG).show()
        return
    }

    val usbDir = File(usbPath, "ScoutingReports")
    if (!usbDir.exists()) {
        usbDir.mkdirs()
    }

    var successCount = 0

    for (file in csvFiles) {
        val destFile = File(usbDir, file.name)
        try {
            file.copyTo(destFile, overwrite = true)
            successCount++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Toast.makeText(context, "Exported $successCount files to USB", Toast.LENGTH_LONG).show()
}

/*fun detectUsbPath(): String? {
    val possiblePaths = File("/storage").listFiles()?.map { it.absolutePath }
    return possiblePaths?.find { it.contains("usb") || it.matches(Regex("/storage/[0-9A-F]{4}-[0-9A-F]{4}")) }
}

fun detectUsbPath(context: Context): String? {
    val externalDirs = context.getExternalFilesDirs(null)
    for (dir in externalDirs) {
        if (dir != null && !dir.absolutePath.contains("emulated")) {
            return dir.absolutePath.substringBefore("/Android") // Get the root USB path
        }
    }
    return null
}*/

fun detectUsbPath(): String? {
    val storageDir = File("/storage")
    val possiblePaths = storageDir.listFiles()?.map { it.absolutePath }

    if (possiblePaths != null) {
        for (path in possiblePaths) {
            Log.d("USB_DEBUG", "Checking path: $path")
            if (path.matches(Regex("/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}"))) {
                Log.d("USB_DEBUG", "Detected USB storage path: $path")
                return path
            }
        }
    }

    Log.d("USB_DEBUG", "No USB drive detected")
    return null
}







@Preview(showBackground = true)
@Composable
fun ViewReportsPreview(){
    ScoutTheme{
        val navController = TestNavHostController(LocalContext.current)
        ViewReports(navController)
    }
}