package com.example.balansapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.firstcomposeap.ui.notification.ReminderWorker
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BalansApp Channel"
            val descriptionText = "Kanał powiadomień przypomnień"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("BalansApp Channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
//        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
//            6, TimeUnit.HOURS
//        )
//            .setInitialDelay(3, TimeUnit.HOURS)
//            .build()
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            "training_and_meal_reminder",
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )




        val testRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(25, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(testRequest)

    }
}