package com.example.scheduleexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import java.util.*
import java.util.concurrent.TimeUnit

class SchedularReceiver : BroadcastReceiver() {
    private val TAG = "AlarmReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "BroadcastReceive")

        setRepetitiveAlarm(SchedulerHelper(context))
    }

    //1 Day
    private fun setRepetitiveAlarm(schedulerHelper: SchedulerHelper) {

        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(1)
            Log.d(TAG,"Set alarm for next day same time - ${convertDate(this.timeInMillis)}")
        }
        schedulerHelper.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertDate(timeInMillis: Long): String =
            DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

    class AlarmBootReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                SchedulerHelper(context).setRepetitiveAlarm()
            }
        }
    }

    class TimeChangedReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {

            if (intent?.action == "android.intent.action.TIME_SET") {
                SchedulerHelper(context).setRepetitiveAlarm()
            }
        }
    }

}