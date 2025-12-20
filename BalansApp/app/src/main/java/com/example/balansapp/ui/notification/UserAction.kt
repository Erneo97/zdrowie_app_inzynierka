package com.example.firstcomposeap.ui.notification
import androidx.datastore.preferences.core.longPreferencesKey

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit

object UserAction {
    val LAST_MEAL_DATE = longPreferencesKey("last_meal_date")
    val LAST_TRENING_DATE = longPreferencesKey("last_trening_date")
}

suspend fun saveLastMealAction(context: Context) {
   Log.e("ReminderWorker", "saveLastMealAction")
    context.dataStore.edit {
        it[UserAction.LAST_MEAL_DATE] = System.currentTimeMillis()
    }
}

suspend fun saveLastTreningAction(context: Context) {
    Log.e("ReminderWorker", "saveLastTreningAction")
    context.dataStore.edit {
        it[UserAction.LAST_TRENING_DATE] = System.currentTimeMillis()
    }
}