package com.example.scheduleexample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class SchedulerHelper(private val context: Context) {
    private val TAG = "AlarmHelper"
    private val alarmManager: AlarmManager? =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setRepetitiveAlarm() {
        Log.e(TAG, "Alarm Set")
            val currentDate:Date= Date()
            val currentTime:Calendar= Calendar.getInstance()
            currentTime.time=currentDate
            val scheduleTime: Calendar = Calendar.getInstance().apply {
                time = currentDate
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 1)
            }
            if (scheduleTime.before(currentTime))
                scheduleTime.add(Calendar.DATE,1)
            setRepetitiveAlarm(scheduleTime.timeInMillis)
    }

    fun cancelRepetitiveAlarm(){
        alarmManager?.let {
            alarmManager.cancel(getPendingIntent())
        }
    }

    fun setRepetitiveAlarm(timeInMillis: Long) {
        Log.e(TAG, "Alarm Set To : "+timeInMillis)
        setAlarm(
                timeInMillis,
                getPendingIntent()
        )
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, SchedularReceiver::class.java)
                .apply {
                    action = "ACTION_SET_REPETITIVE_EXACT"
                }
        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            timeInMillis,
                            pendingIntent
                    )
                }else{
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            timeInMillis,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                    )
                }
            }
        }
    }

    fun enableBootRepeating(applicationContext:Context){
        val receiver = ComponentName(applicationContext, SchedularReceiver.AlarmBootReceiver::class.java)
        applicationContext.packageManager?.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
    }
}