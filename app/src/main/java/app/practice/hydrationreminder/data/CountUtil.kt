package app.practice.hydrationreminder.data

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class CountUtil(private val sharedPreferences: SharedPreferences) {

    private var editor: Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_WATER_COUNT = "water-count"
        private const val KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count"
        private const val DEFAULT_COUNT = 0
    }

    fun setWaterCount(glassesOfWater: Int) {
        synchronized(this) {
            editor.putInt(KEY_WATER_COUNT, glassesOfWater)
            editor.commit()
        }
    }

    fun getWaterCount(): Int {
        return sharedPreferences.getInt(KEY_WATER_COUNT, DEFAULT_COUNT)
    }

    fun incrementWaterCount() {
        synchronized(this) {
            val currentWaterCount = getWaterCount()
            setWaterCount(currentWaterCount.inc())
        }
    }

    fun getChargingReminderCount(): Int {
        return sharedPreferences.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT)
    }

    fun incrementChargingReminderCount() {
        synchronized(this) {
            val currentChargingReminderCount = getChargingReminderCount()
            editor.putInt(KEY_CHARGING_REMINDER_COUNT, currentChargingReminderCount.inc())
        }
    }
}