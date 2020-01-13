package app.practice.hydrationreminder.sync

import app.practice.hydrationreminder.util.Injection
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import kotlinx.coroutines.*

class WaterReminderFirebaseJobService : JobService() {

    private val job: Job = Job()

    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    override fun onStartJob(params: JobParameters): Boolean {
        ioScope.launch {
            withContext(Dispatchers.Default) {
                val context = this@WaterReminderFirebaseJobService
                val reminderTasks = Injection.provideReminderTasks(context)
                reminderTasks.executeTask(ReminderTasks.ACTION_CHARGING_REMINDER)
            }
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        job.cancel()
        return true
    }
}