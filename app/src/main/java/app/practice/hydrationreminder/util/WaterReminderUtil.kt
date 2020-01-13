package app.practice.hydrationreminder.util

import android.content.Context
import app.practice.hydrationreminder.sync.WaterReminderFirebaseJobService
import com.firebase.jobdispatcher.*
import java.util.concurrent.TimeUnit

class WaterReminderUtil(private val context: Context) {

    companion object {
        private const val REMINDER_INTERVAL_MINUTES = 15L
        private var REMINDER_INTERVAL_SECONDS =
            TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES).toInt()
        private val SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS
        private const val REMINDER_JOB_TAG = "hydration-reminder-tag"
    }

    private var isInitialized = false

    fun scheduleChargingReminder() {
        if (isInitialized) return
        synchronized(this) {
            val driver = GooglePlayDriver(context)
            val jobDispatcher = FirebaseJobDispatcher(driver)
            val waterReminderJob = jobDispatcher.newJobBuilder().run {
                setService(WaterReminderFirebaseJobService::class.java)
                setTag(REMINDER_JOB_TAG)
                setConstraints(Constraint.DEVICE_CHARGING)
                lifetime = Lifetime.FOREVER
                isRecurring = true
                trigger = Trigger.executionWindow(
                    REMINDER_INTERVAL_SECONDS,
                    REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
                )
                setReplaceCurrent(true)
                build()
            }
            jobDispatcher.schedule(waterReminderJob)
            isInitialized = true
        }
    }
}