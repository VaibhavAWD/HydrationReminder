package app.practice.hydrationreminder.sync

import android.app.IntentService
import android.content.Intent
import app.practice.hydrationreminder.util.Injection

class WaterReminderIntentService : IntentService("WaterReminderIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                val reminderTasks = Injection.provideReminderTasks(this)
                reminderTasks.executeTask(action)
            }
        }
    }
}