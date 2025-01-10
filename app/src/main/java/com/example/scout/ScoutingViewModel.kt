package com.example.scout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scout.database.ScoutingInputFields
import com.example.scout.database.ScoutingRepository
import kotlinx.coroutines.launch

class ScoutingViewModel(private val repository: ScoutingRepository) : ViewModel() {

    private val _fieldsForAutonomous = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForAutonomous: LiveData<List<ScoutingInputFields>> = _fieldsForAutonomous

    private val _fieldsForTeleop = MutableLiveData<List<ScoutingInputFields>>()
    val fieldsForTeleop: LiveData<List<ScoutingInputFields>> = _fieldsForTeleop

    // Call this function to preload the database with default fields
    /*init {
        viewModelScope.launch {
            repository.preloadDatabase()
            //loadFieldsForAutonomous()
            //loadFieldsForTeleop()
        }
    }*/

    // Load fields for the "Autonomous" section
    private fun loadFieldsForAutonomous() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Autonomous")
            _fieldsForAutonomous.postValue(fields)
        }
    }

    // Load fields for the "Teleop" section
    private fun loadFieldsForTeleop() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Teleop")
            _fieldsForTeleop.postValue(fields)
        }
    }

    private fun loadFieldsForEndgame() {
        viewModelScope.launch {
            val fields = repository.getFieldsForSection("Endgame")
            _fieldsForTeleop.postValue(fields)
        }
    }


    // Insert a new field into the database
    fun insertField(field: ScoutingInputFields) {
        viewModelScope.launch {
            repository.insertField(field)
        }
    }

    // Delete a field from the database
    fun deleteField(field: ScoutingInputFields) {
        viewModelScope.launch {
            repository.deleteField(field)
        }
    }
}
