package com.example.scout

import androidx.lifecycle.ViewModel

class TeamViewModel: ViewModel() {
    // Variables saved to view model pertaining to team information
    var teamNumber: String? = null
    var eventName: String? = null
    var teamBeingScouted: String? = null
}