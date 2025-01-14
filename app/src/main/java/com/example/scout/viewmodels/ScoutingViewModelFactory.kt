package com.example.scout.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scout.database.ScoutingRepository

class ScoutingViewModelFactory(private val repository: ScoutingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoutingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoutingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
