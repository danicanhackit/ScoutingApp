package com.example.scout.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.scout.database.ScoutingInputFields
import com.example.scout.database.ScoutingReport
import com.example.scout.database.ScoutingRepository
import kotlinx.coroutines.launch

class ScoutingViewModel(private val repository: ScoutingRepository) : ViewModel() {

    private val _fieldsForAutonomous = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForAutonomous: LiveData<List<ScoutingInputFields>> = _fieldsForAutonomous

    private val _fieldsForTeleop = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForTeleop: LiveData<List<ScoutingInputFields>> = _fieldsForTeleop

    private val _fieldsForEndgame = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForEndgame: LiveData<List<ScoutingInputFields>> = _fieldsForEndgame

    var reportId: String = ""

    // Call this function to preload the database with default fields
    init {
        viewModelScope.launch {
            repository.preloadDatabase()
        }
    }

    // Load fields for the "Autonomous" section
    fun loadFieldsForAutonomous() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Autonomous")
            _fieldsForAutonomous.postValue(fields)
        }
    }

    // Load fields for the "Teleop" section
    fun loadFieldsForTeleop() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Teleop")
            _fieldsForTeleop.postValue(fields)
        }
    }

    fun loadFieldsForEndgame() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Endgame")
            _fieldsForTeleop.postValue(fields)
        }
    }


    // Insert a new field into the database
    fun insertFieldToScoutingInputFields(field: ScoutingInputFields) {
        viewModelScope.launch {
            repository.insertFieldToScoutingInputFields(field)
        }
    }

    // Delete a field from the database
    fun deleteFieldFromScoutingInputFields(field: ScoutingInputFields) {
        viewModelScope.launch {
            repository.deleteFieldFromScoutingInputFields(field)
        }
    }

    fun addScoutingReport(field: ScoutingReport) {
        viewModelScope.launch {
            repository.addScoutingReport(field)
        }
    }

    fun deleteScoutingReport(field: ScoutingReport) {
        viewModelScope.launch {
            repository.deleteScoutingReport(field)
        }
    }

    fun exportReportById(context: Context, reportId: String){
        viewModelScope.launch {
            repository.exportReportById(context, reportId)
        }
    }

    fun generateReportId(): String {
        return UUID.randomUUID().toString() // Generates a unique string every time
    }

}
