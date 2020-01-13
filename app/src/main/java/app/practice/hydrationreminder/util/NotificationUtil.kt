package app.practice.hydrationreminder.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.practice.hydrationreminder.MainActivity
import app.practice.hydrationreminder.R
import app.practice.hydrationreminder.sync.ReminderTasks
import app.practice.hydrationreminder.sync.WaterReminderIntentService

class NotificationUtil(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "hydration-reminder-notification-channel"
        private const val CHANNEL_NAME = "Hydration Reminder Notification Channel"
        private const val NOTIFICATION_ID = 1234
        private const val PENDING_INTENT_ID = 4321
        private const val ACTION_DRINK_PENDING_INTENT_ID = 100
        private const val ACTION_IGNORE_PENDING_INTENT_ID = 200
    }

    private lateinit var notificationManager: NotificationManager

    fun remindBacauseCharging() {
        createNotificationChannel()
        notificationManager.notify(NOTIFICATION_ID, buildNotification())
    }

    fun clearAllNotification() {
        notificationManager.cancelAll()
    }

    private fun buildNotification(): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        builder.apply {
            setContentTitle(context.getString(R.string.charging_reminder_notification_title))
            setContentText(context.getString(R.string.charging_reminder_notification_body))
            color = ContextCompat.getColor(context, R.color.colorPrimary)
            setSmallIcon(R.drawable.ic_drink_notification)
            setLargeIcon(getLargeIcon())
            setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.charging_reminder_notification_body)
                )
            )
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(Notification.DEFAULT_ALL)
            setContentIntent(getContentIntent())
            addAction(createDrinkWaterAction())
            addAction(createIgnoreReminderAction())
            setAutoCancel(true)
        }
        return builder.build()
    }

    private fun createDrinkWaterAction(): NotificationCompat.Action {
        val incrementWaterCountIntent = Intent(context, WaterReminderIntentService::class.java)
        incrementWaterCountIntent.action = ReminderTasks.ACTION_INCREMENT_WATER_COUNT
        val incrementWaterCountPendingIntent = PendingIntent.getService(
            context,
            ACTION_DRINK_PENDING_INTENT_ID,
            incrementWaterCountIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(
            R.drawable.ic_drink_action,
            context.getString(R.string.notification_action_i_did_it),
            incrementWaterCountPendingIntent
        )
    }

    private fun createIgnoreReminderAction(): NotificationCompat.Action {
        val ignoreReminderIntent = Intent(context, WaterReminderIntentService::class.java)
        ignoreReminderIntent.action = ReminderTasks.ACTION_DISMISS_NOTIFICATION
        val ignoreReminderPendingIntent = PendingIntent.getService(
            context,
            ACTION_IGNORE_PENDING_INTENT_ID,
            ignoreReminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(
            R.drawable.ic_cancel_action,
            context.getString(R.string.notification_action_no_thanks),
            ignoreReminderPendingIntent
        )
    }

    private fun getContentIntent(): PendingIntent {
        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_ID,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getLargeIcon(): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.ic_drink_notification)
    }

    private fun createNotificationChannel() {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}