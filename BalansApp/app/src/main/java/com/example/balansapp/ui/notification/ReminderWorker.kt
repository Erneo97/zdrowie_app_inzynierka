package com.example.firstcomposeap.ui.notification

import android.Manifest
import android.R
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class ReminderWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        Log.d("ReminderWorker", "🚀 Worker uruchomiony!")

        val prefs = applicationContext.dataStore.data.first()
        val lastAction = prefs[UserAction.LAST_TRENING_DATE] ?: return Result.success()
        val lastActionMeal = prefs[UserAction.LAST_MEAL_DATE] ?: return Result.success()
        val daysDiff =
            TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - lastAction
            )
        Log.d("ReminderWorker", "daysDiff ${daysDiff}")
        if (daysDiff >= 2) {
            showTreningNotification(daysDiff)
        }



        val hoursDiff =
            TimeUnit.MILLISECONDS.toSeconds(
                System.currentTimeMillis() - lastActionMeal
            )
        Log.d("ReminderWorker", "hoursDiff ${hoursDiff}")
        if (hoursDiff >= 12) {
            showMealNotification(hoursDiff)
        }

        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showTreningNotification(days: Long) {
        val text = when (days) {
            2L -> "Minęły 2 dni bez treningu 💪"
            3L -> "3 dni przerwy – czas wrócić!"
            else -> "Nie trenowałeś od kilku dni"
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            "REMINDER_CHANNEL"
        )
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle("Czas na trening")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .notify(1, notification)
    }
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showMealNotification(hours: Long) {
        val text = when (hours) {
            12L -> "Mineło sporo czasu od posiłku - czas napełnić brzuch"
            13L -> "Zaczynam się martwić o twoją dietę"
            else -> "Nie jesz już zdecydowanie za długo - twój brzuch będzię smutny"
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            "REMINDER_CHANNEL"
        )
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle("Czas jeść")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .notify(1, notification)
    }

}