package com.example.scout.viewmodels

import androidx.lifecycle.ViewModel

class TeamViewModel: ViewModel() {
    // Variables saved to view model pertaining to team information
    var teamNumber: String? = null
    var eventName: String? = null
    var year: Int? = null
    var teamBeingScouted: String? = null

    var dataFieldSection: String? = null

}