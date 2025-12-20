package com.example.balansapp

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.firstcomposeap.ui.notification.ReminderWorker
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            6, TimeUnit.HOURS
        )
            .setInitialDelay(3, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "training_and_meal_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )


//        val testRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
//            .setInitialDelay(10, TimeUnit.SECONDS)
//            .build()
//
//        WorkManager.getInstance(this).enqueue(testRequest)

    }
}