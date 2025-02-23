package com.example.scout.files
import android.content.Context
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
