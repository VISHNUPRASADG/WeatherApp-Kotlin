package com.example.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.GPS.GpsTracker
import com.example.weatherapp.roomDB.DatabaseBuilder
import com.example.weatherapp.roomDB.DatabaseHelperImpl
import com.example.weatherapp.roomDB.LocationData
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.runBlocking


class MyWorker (context : Context, params : WorkerParameters)
    : Worker(context, params){

     val mContext: Context = context
    val dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))

    override fun doWork(): Result {

        Log.d("location periodic call","Uploading location in shared pref")
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
          getLocation()
        }, 0)
        sendNotification("Fetching Location","Succcessfully done")
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            "default"
        )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }

    fun getLocation() {
        val gpsTracker = GpsTracker(mContext)

        //current date and time
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        Log.d("Date",currentDateAndTime)

        if(gpsTracker.canGetLocation()){
            if(gpsTracker.getLat()?.equals(null) == false)
            runBlocking {
                val locationData = LocationData(null,gpsTracker.getLat(),gpsTracker.getLong(),currentDateAndTime)
                Log.d("LocationCheckWM",locationData.toString())
                dbHelper.insertLocationData(locationData)
                Log.d("LocationDBCheckWM",dbHelper.getLocationData().toString())
            }
            else
                getLocation()

        }else{
          //  gpsTracker.showSettingsAlert();
        }
    }

}