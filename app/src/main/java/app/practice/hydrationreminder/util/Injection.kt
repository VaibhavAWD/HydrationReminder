package app.practice.hydrationreminder.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import app.practice.hydrationreminder.data.CountUtil
import app.practice.hydrationreminder.sync.ReminderTasks

object Injection {

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun provideCountUtil(context: Context): CountUtil {
        return CountUtil(provideSharedPreferences(context))
    }

    fun provideReminderTasks(context: Context): ReminderTasks {
        return ReminderTasks(context)
    }
}