package app.practice.hydrationreminder.sync

import android.content.Context
import app.practice.hydrationreminder.data.CountUtil
import app.practice.hydrationreminder.util.Injection

class ReminderTasks(context: Context) {

    companion object {
        const val ACTION_INCREMENT_WATER_COUNT = "increment-water-count"
        const val ACTION_DISMISS_NOTIFICATION = "dismiss-notification"
        const val ACTION_CHARGING_REMINDER = "charging-reminder"
    }

    private var countUtil: CountUtil = Injection.provideCountUtil(context)
    private var notificationUtil = Injection.provideNotificationUtil(context)

    fun executeTask(action: String) {
        when (action) {
            ACTION_INCREMENT_WATER_COUNT -> incrementWaterCount()
            ACTION_DISMISS_NOTIFICATION -> dismissNotification()
            ACTION_CHARGING_REMINDER -> issueChargingReminder()
        }
    }

    private fun incrementWaterCount() {
        countUtil.incrementWaterCount()
    }

    private fun dismissNotification() {
        notificationUtil.clearAllNotification()
    }

    private fun issueChargingReminder() {
        countUtil.incrementChargingReminderCount()
    }

}