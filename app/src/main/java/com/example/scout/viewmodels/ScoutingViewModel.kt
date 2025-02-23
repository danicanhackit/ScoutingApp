package com.example.scout.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.scout.database.ScoutingInputFields
import com.example.scout.database.ScoutingReport
import com.example.scout.database.ScoutingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScoutingViewModel(private val repository: ScoutingRepository) : ViewModel() {

    private val _fieldsForAutonomous = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForAutonomous: LiveData<List<ScoutingInputFields>> = _fieldsForAutonomous

    private val _fieldsForTeleop = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForTeleop: LiveData<List<ScoutingInputFields>> = _fieldsForTeleop

    private val _fieldsForEndgame = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForEndgame: LiveData<List<ScoutingInputFields>> = _fieldsForEndgame

    val allInputFieldNames: LiveData<List<String>> = repository.allInputFieldNames

    private val _selectedField = MutableStateFlow<ScoutingInputFields?>(null)
    val selectedField: StateFlow<ScoutingInputFields?> = _selectedField

    private val _reportsBySection = MutableLiveData<List<ScoutingReport>>()
    val reportsBySection: LiveData<List<ScoutingReport>> = _reportsBySection

    private val _reportsByTeamNum = MutableLiveData<List<ScoutingReport>>()
    val reportsByTeamNum: LiveData<List<ScoutingReport>> = _reportsByTeamNum

    private val _reportsById = MutableLiveData<List<ScoutingReport>>()
    val reportsById: LiveData<List<ScoutingReport>> = _reportsById

    var reportId: String = ""
    var fieldToDelete: String? = null

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
            _fieldsForEndgame.postValue(fields)
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

    fun fetchFieldByFieldName(fieldName: String) {
        viewModelScope.launch {
            val field: ScoutingInputFields = repository.getFieldByFieldName(fieldName)
            _selectedField.value = field
        }
    }

    fun getReportFieldsBySection(id: String, section: String){
        viewModelScope.launch {
            val fields = repository.getReportsByIdAndSection(id, section)
            _reportsBySection.postValue(fields)
        }
    }

    fun getReportFieldsByTeamNum(id: String, teamNum: Int?){
        viewModelScope.launch {
            val fields = repository.getReportsByIdAndTeamNum(id, teamNum)
            _reportsByTeamNum.postValue(fields)
        }
    }

    fun getReportsById(id: String){
        viewModelScope.launch {
            val fields = repository.getReportsById(id)
            _reportsById.postValue(fields)
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

    fun exportReportById(context: Context, reportId: String, teamNum: String){
        viewModelScope.launch {
            repository.exportReportById(context, reportId, teamNum)
        }
    }

    fun generateReportId(): String {
        return UUID.randomUUID().toString() // Generates a unique string every time
    }

}
