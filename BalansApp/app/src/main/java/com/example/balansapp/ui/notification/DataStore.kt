package com.example.firstcomposeap.ui.notification

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "user_prefs")