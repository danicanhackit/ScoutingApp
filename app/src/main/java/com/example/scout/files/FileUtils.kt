package com.example.scout.files
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.scout.database.ScoutingReport
import java.io.File
import java.io.FileWriter
import java.io.IOException

object FileUtils {

    fun exportDatabaseToCSV(context: Context, filename: String, data: List<ScoutingReport>) {
        val scoutingDir = File(context.getExternalFilesDir(null), "ScoutingReports")
        if (!scoutingDir.exists()) {
            scoutingDir.mkdirs()
        }
        val file = File(scoutingDir, filename)

        try {
            val writer = FileWriter(file)
            // Write the header
            writer.append("ReportID,TeamNum,Section,FieldName,FieldValue\n")
            // Write each entry
            for (entry in data) {
                writer.append("${entry.reportId},${entry.teamNumberBeingScouted},${entry.gameplaySection},${entry.fieldName},${entry.enteredValue}\n")
            }
            writer.flush()
            writer.close()

            FileProvider.getUriForFile(context, "com.example.scout.files", file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun shareCSV(context: Context, fileUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Required for sharing
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share CSV via"))
    }
}
